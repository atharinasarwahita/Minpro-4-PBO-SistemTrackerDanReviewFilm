*Mini Project (Minpro) 4 - Pemrograman Berbasis Objek (PBO)*  

---

*Nama: Atha Rina Sarwahita*  
*NIM: 2509116047*

# 🎬🍿 Sistem Tracker & Review Film  

> Sistem berbasis CLI (Java) untuk mencatat film yang ditonton dan menulis review, sistem ini dibangun dengan prinsip PBO dan arsitektur **MVC**.

## Deskripsi Singkat ─ ★ ˙ ̟ !!
Program ini membantu pengguna mengelola daftar film, baik **Feature Film** maupun **Animated Film**, sekaligus menambahkan **review** untuk tiap film. Terdapat juga fitur Create, Read, Update, dan Delete (CRUD) yang lengkap pada sistem ini, sperti menambah film, menambah review, lihat film dan review, update review, serta delete review. Seluruh input dicek oleh class validasi khusus agar data tetap konsisten.
 
Data disimpan di database **MySQL (`db_film_review`)** menggunakan **JDBC**, sehingga tidak hilang saat program ditutup. Seluruh query SQL dipisahkan ke class **DAO**, dan koneksi database dikelola di satu tempat oleh class `Koneksi`. Saat program dijalankan, koneksi dicek terlebih dahulu dan ditutup kembali ketika user keluar dari program.

## Struktur Package ─ ★ ˙ ̟ !!
```
src/main/java/
├── config/
│   ├── Koneksi.java            → Membuat satu koneksi JDBC bersama ke MySQL, dan menutupnya saat keluar
│   └── TestKoneksi.java        → Penguji keberhasilan koneksi database
├── controller/
│   ├── FilmController.java     → Mengatur logika bisnis, alur program, dan DAO
│   └── ValidasiInput.java      → Memvalidasi seluruh input pengguna dari CLI
├── main/
│   └── Main.java               → Entry point utama untuk menjalankan aplikasi
├── model.dao/
│   ├── AnimatedFilmDAO.java    → Akses database khusus tabel film animasi (JDBC)
│   ├── FeatureFilmDAO.java     → Akses database khusus tabel feature film (JDBC)
│   └── ReviewDAO.java          → Akses database khusus tabel review/ulasan (JDBC)
├── model.entitas/
│   ├── AnimatedFilm.java       → Subclass entitas untuk film animasi
│   ├── Displayable.java        → Interface untuk kontrak tampilan data
│   ├── FeatureFilm.java        → Subclass entitas untuk feature film (live-action)
│   ├── Film.java               → Abstract superclass entitas film
│   └── Review.java             → Entitas data ulasan film
└── view/
    └── FilmView.java           → Menampilkan menu utama dan antarmuka CLI
```

| Package | Peran |
|---|---|
| `main` | Menjalankan program dan menampilkan menu utama |
| `config` | Konfigurasi koneksi database (URL, user, password) |
| `model.entitas` | Data dan aturan bisnis (Film, Review, turunan, interface) |
| `model.dao` | Satu-satunya tempat yang berisi query SQL dan berinteraksi dengan database |
| `view` | Antarmuka console, tanpa logika bisnis |
| `controller` | Jembatan view, DAO, dan model; mengolah logika serta validasi input |

---

## Alur Program ─ ★ ˙ ̟ !!

```text
┌──────────────────────────────┐
│         Main.java            │  Program dijalankan
│  (cek koneksi database)      │
└──────────────┬───────────────┘
               ▼
┌──────────────────────────────┐
│         FilmView             │◄─────────────────────────────┐
│      Menampilkan menu        │                              │
└──────────────┬───────────────┘                              │
               ▼                                              │
        ┌─────────────┐                                       │
        │ Pilih menu? │── 6. Keluar -> Tutup koneksi, selesai │
        └──────┬──────┘                                       │
               ▼                                              │
┌──────────────────────────────┐                              │
│   ValidasiInput              │  Input tidak valid ->        │
│   Membaca & memeriksa input  │  minta user mengulang        │
└──────────────┬───────────────┘                              │
               ▼                                              │
┌──────────────────────────────┐                              │
│       FilmController         │                              │
│  Memproses permintaan user   │                              │
└──────────────┬───────────────┘                              │
               ▼                                              │
┌──────────────────────────────┐                              │
│  DAO (Feature/Animated/      │                              │
│  Review) + PreparedStatement │                              │
└──────────────┬───────────────┘                              │
               ▼                                              │
┌──────────────────────────────┐                              │
│  Koneksi -> MySQL            │                              │
│  (db_film_review)            │                              │
└──────────────┬───────────────┘                              │
               ▼                                              │
┌──────────────────────────────┐                              │
│  Entitas (Film / Review)     │                              │
│  dibentuk dari hasil query   │                              │
└──────────────┬───────────────┘                              │
               ▼                                              │
┌──────────────────────────────┐                              │
│         FilmView             │                              │
│  Menampilkan hasil ke user   │──────────────────────────────┘
└──────────────────────────────┘     Kembali ke menu utama
```

1. `Main` memeriksa koneksi database lebih dulu lewat `Koneksi.getConnection()`. Jika gagal (misalnya MySQL belum aktif), program menampilkan pesan dan berhenti. Jika berhasil, `Main` menampilkan menu lewat `FilmView` dan membaca pilihan user (1-6) lewat `ValidasiInput`.
2. Pilihan menu diteruskan ke method yang sesuai di `FilmController`.
3. Controller meminta input tambahan dari user. Input diperiksa oleh `ValidasiInput` dan setter pada model; jika salah, user diminta mengulang.
4. Data yang valid dibungkus menjadi objek entitas (`FeatureFilm` / `AnimatedFilm` / `Review`), lalu dikirim ke DAO.
5. DAO memakai koneksi bersama dari `Koneksi` (diambil sekali di constructor), menjalankan query dengan `PreparedStatement`, dan mengembalikan hasilnya (list objek entitas) ke controller.
6. Controller menyerahkan data ke `FilmView` untuk ditampilkan, lalu program kembali ke menu utama.
7. Saat user memilih menu 6 (Keluar), `Main` memanggil `Koneksi.tutupKoneksi()` untuk menutup koneksi sebelum program berhenti.

**Alur khusus Rewatch Log.**  
Saat user menambah review, controller memanggil `reviewDAO.ambilReviewId(idFilm)`. Jika hasilnya tidak kosong, berarti film sudah pernah diulas, sehingga review baru otomatis disimpan dengan `is_rewatch = true` dan ditampilkan dengan penanda `[REWATCH]`.
 
| Menu | Method Controller | Method DAO yang dipanggil |
|---|---|---|
| 1. Tambah Film | `tambahFilm()` | `tambahFeatureFilm()` / `tambahAnimatedFilm()` |
| 2. Tambah Review | `tambahReview()` | `ReviewDAO.adaIdFilm()`, `ambilReviewId()`, `tambahReview()` |
| 3. Lihat Film & Review | `lihatFilm()` | `ambilFilm()` (feature & animated), `ambilSemuaReview()` |
| 4. Update Review | `updateReview()` | `ambilSemuaReview()`, `updateReview()` |
| 5. Hapus Review | `hapusReview()` | `ambilSemuaReview()`, `hapusReview()` |
| 6. Keluar | `Main` | `Koneksi.tutupKoneksi()` (menutup koneksi) |

---

## Penerapan Encapsulation & Inheritance ─ ★ ˙ ̟ !!
**Encapsulation**  

Atribut pada `Film` (`idFilm`, `judul`, `sutradara`, `tahunRilis`, `genre`, `durasi`) dideklarasikan `protected`, sehingga tidak bisa diakses langsung dari luar package tetapi tetap bisa dipakai oleh subclass-nya. Atribut khas di `FeatureFilm` (`pemeranUtama`, `karakterLiveAction`), di `AnimatedFilm` (`pengisiSuara`, `karakterAnimasi`, `gayaAnimasi`), dan seluruh atribut di `Review` dideklarasikan `private`. Data tersebut hanya bisa dibaca dan diubah lewat getter dan setter.

Setter pada program ini tidak hanya mengisi nilai, tetapi memeriksa dulu apakah nilainya valid. Setter yang berisi validasi mengembalikan nilai `boolean` (`true` jika diterima, `false` jika ditolak), sehingga controller tahu kapan harus meminta user mengisi ulang. Validasi yang ada di dalam setter:
- `setTahunRilis()` di `Film`: tahun harus antara 1888 (tahun film pertama dibuat) sampai tahun sekarang. Batas atasnya diambil otomatis dari `Year.now()`, jadi tidak perlu diubah manual setiap tahun.
- `setDurasi()` di `Film`: durasi minimal 30 menit, mengikuti standar film pendek.
- `setRating()` di `Review`: rating harus antara 1.0 sampai 5.0.

Validasi ini juga berjalan saat objek dibuat, karena constructor `Film` memanggil `setTahunRilis()` dan `setDurasi()`, sedangkan constructor `Review` memanggil `setRating()`. Dengan begitu, objek tidak bisa dibuat dengan data yang melanggar aturan.

---

Keyword `final` dipakai pada nilai yang tidak boleh berubah setelah dibuat:
- `idFilm` di `Film`, serta `idReview`, `idFilm`, dan `isRewatch` di `Review`.
- Method `getIdFilm()` di `Film`, sehingga tidak bisa di-override oleh subclass.
- Parameter `idFilm` pada constructor `Film`, `FeatureFilm`, dan `AnimatedFilm`.
- Atribut `featureDAO`, `animatedDAO`, `reviewDAO`, dan `view` di `FilmController`, sehingga referensinya tidak bisa diganti.
- Konstanta koneksi `URL`, `USER`, dan `PASS` di `Koneksi` (`private static final`).
  
Atribut `conn` di `Koneksi` (`private static`) dan di setiap DAO (`private Connection conn`) juga bersifat `private`, sehingga koneksi hanya bisa diperoleh lewat `Koneksi.getConnection()` dan ditutup lewat `Koneksi.tutupKoneksi()`, tidak diakses langsung dari luar class.

ID film dan review dibuat otomatis oleh database lewat `AUTO_INCREMENT`. Karena itu objek baru dibuat dengan ID `0` (placeholder) dan user tidak dapat memasukkan ID sendiri.

---

**Inheritance**
- `FeatureFilm extends Film` dan `AnimatedFilm extends Film`. Konsep pewarisan ini diterapkan pada Superclass `Film`, lalu diwariskan ke Subclass `FeatureFilm` dan `AnimatedFilm`
  
<img width="600" alt="image" src="https://github.com/user-attachments/assets/24b7346d-774d-4faa-8ebd-b5b16d18b9e4" />

<img width="600" alt="image" src="https://github.com/user-attachments/assets/257d60ef-1675-4da9-ad70-cea2ce2087ba" />

- Atribut dan method umum (judul, tahun, durasi, dll.) diwarisi dari `Film`; tiap subclass menambah atribut khasnya.

<img width="1021" height="89" alt="image" src="https://github.com/user-attachments/assets/ed5bcc04-9451-496c-843a-661f250af560" />

  Pada Subclass FeatureFilm, terdapat atribut khas yaitu Pemeran Utama (Aktor), dan Karakter Live Action (Karakter yg diperankan)

<img width="1017" height="110" alt="image" src="https://github.com/user-attachments/assets/31c74cec-5c0e-44ca-9189-28dabad34cb2" />

  Pada Subclass AnimatedFilm, terdapat 3 atribut khas yaitu Pengisi Suara, Karakter Animasi, dan Gaya Animasi.

## Penerapan Polymorphism & Abstraction ─ ★ ˙ ̟ !!  

**Abstraction**
- `Film` berupa `abstract class` dengan `abstract method` `tampilkanDetailSpesifik()`  yang wajib diimplementasikan subclass.
  - **Abstract Class**

    <img width="704" height="129" alt="image" src="https://github.com/user-attachments/assets/09a36789-e4d5-48f0-ab6e-265b90e98de2" />

  - **Abstract Method**
  
    <img width="705" height="54" alt="image" src="https://github.com/user-attachments/assets/8923faa0-ce44-4998-9914-3171ab9b7289" />


**Polymorphism**
- *Overriding*: Karena `tampilkanDetailSpesifik()` bersifat abstract, `FeatureFilm` dan `AnimatedFilm` **wajib mengimplementasikannya** dengan `@Override`. Tiap subclass mengisinya sesuai atribut masing-masing, konsep Override ini memungkinkan nama method yang sama, tetapi hasilnya berbeda.
  - Subclass AnimatedFilm
  
    <img width="840" height="126" alt="image" src="https://github.com/user-attachments/assets/f8167497-526c-4f49-8668-cb6955fc328b" />

  - Subclass FeatureFilm
  
    <img width="841" height="145" alt="image" src="https://github.com/user-attachments/assets/b592ab6b-72db-493c-ae11-f353f24f48f5" />

- *Overloading* adalah membuat beberapa method dengan **nama yang sama** tetapi **parameter berbeda**. Di program ini, method `tampilkanInfo()` diterapkan di dua class, yaitu `Film` dan `Review`, dengan dua versi:

  | Versi | Parameter | Fungsi |
  |---|---|---|
  | `tampilkanInfo()` | tanpa parameter | Menampilkan info **lengkap** (default) |
  | `tampilkanInfo(boolean ringkas)` | satu parameter `boolean` | Menampilkan info **ringkas** jika `true`, **lengkap** jika `false` |

  Versi tanpa parameter tidak menulis ulang logika tampilan, tetapi memanggil versi berparameter dengan nilai `false`. Dengan begitu logika tampilan hanya ditulis sekali dan tidak ada kode yang duplikat.

## Penerapan JDBC ─ ★ ˙ ̟ !!
Program memakai **JDBC** untuk terhubung ke database MySQL `db_film_review`. Seluruh kode JDBC dipisahkan dari controller dan view, yaitu di package `config` (koneksi) dan `model.dao` (query).

### 1. Dependency
Driver MySQL ditambahkan melalui Maven di `pom.xml`:  

<img width="600" alt="image" src="https://github.com/user-attachments/assets/797351c9-fe8f-4f6f-bde7-3bd7961a4e96" />


### 2. Koneksi Database (`config/Koneksi.java`)  

`Koneksi` adalah satu-satunya class yang mengatur koneksi ke database. Koneksi disimpan di atribut `static` sehingga **hanya dibuat satu kali dan dipakai bersama** oleh semua DAO. Jika koneksi belum ada atau sudah tertutup, `getConnection()` membuat yang baru. Jika gagal, method mengembalikan `null` dan menampilkan pesan error.

<img width="600" alt="image" src="https://github.com/user-attachments/assets/786451af-79a7-4c08-ade5-9fa2674e279b" />

Semua DAO memanggil `Koneksi.getConnection()`, sehingga pengaturan koneksi cukup diubah di satu tempat. `TestKoneksi` dipakai untuk mengecek apakah koneksi berhasil sebelum menjalankan program utama.  

**Siklus koneksi dalam program**
1. **Dicek saat awal.** `Main` memanggil `Koneksi.getConnection()` sebelum membuat `FilmController`. Jika hasilnya `null`, program berhenti dengan pesan yang jelas, bukan crash.

   <img width="917" height="110" alt="image" src="https://github.com/user-attachments/assets/4268d499-b14b-494d-836f-cde5b308d35d" />

2. **Dipakai bersama.** `FilmController` membuat ketiga DAO, dan semuanya memakai satu koneksi yang sama selama program berjalan.
3. **Ditutup saat keluar.** Pada menu 6, `Main` memanggil `Koneksi.tutupKoneksi()` sebelum program berhenti.
   <img width="643" height="74" alt="image" src="https://github.com/user-attachments/assets/47d2cfbe-227a-4218-92e7-8b35fcbc624d" />

   `TestKoneksi` dipakai untuk mengecek apakah koneksi berhasil sebelum menjalankan program utama.
   
### 3. Pola DAO (Data Access Object)
DAO adalah class khusus yang mengurus akses data, sehingga controller tidak perlu menulis SQL.
 
| Class DAO | Method | Operasi |
|---|---|---|
| `FeatureFilmDAO` | `tambahFeatureFilm()`, `ambilFilm()` | Insert ke `film` + `feature_film`, select dengan JOIN |
| `AnimatedFilmDAO` | `tambahAnimatedFilm()`, `ambilFilm()` | Insert ke `film` + `animated_film`, select dengan JOIN |
| `ReviewDAO` | `tambahReview()`, `ambilSemuaReview()`, `ambilReviewId()`, `updateReview()`, `hapusReview()`, `adaIdFilm()` | CRUD tabel `review`, serta cek apakah ID film ada |

### 4. PreparedStatement
Seluruh query memakai `PreparedStatement` dengan placeholder `?`, bukan penggabungan string. Nilai diisi lewat `setString()`, `setInt()`, `setDouble()`, dan `setBoolean()`, sehingga aman dari **SQL Injection**. `PreparedStatement` dan `ResultSet` dibuka dengan **try-with-resources** sehingga ditutup otomatis setelah dipakai, sedangkan koneksinya dikelola terpisah oleh `Koneksi`. Setiap `SQLException` ditangkap di DAO dan ditampilkan sebagai pesan, sehingga program tidak langsung berhenti saat query gagal.

- **Create** (`ReviewDAO.tambahReview`)

  <img width="600" alt="image" src="https://github.com/user-attachments/assets/552f6236-7957-49b1-88ca-c035106f6c63" />  


- **Read** (`FeatureFilmDAO.ambilFilm`) memakai `JOIN` karena data film tersebar di dua tabel, lalu `ResultSet` diubah menjadi objek entitas:

  <img width="600" alt="image" src="https://github.com/user-attachments/assets/f8a9281d-7a72-459e-ae13-c7da4187e727" />


- **Update** (`ReviewDAO.updateReview`)

  <img width="600" alt="image" src="https://github.com/user-attachments/assets/febab638-e96f-4ba0-bb52-e7cde073962c" />


- **Delete** (`ReviewDAO.hapusReview`)

  <img width="600" alt="image" src="https://github.com/user-attachments/assets/b9fb46dc-52d2-44cd-9fe4-79566b10be85" />


### 5. Insert ke Dua Tabel dengan Generated Key
Film disimpan di dua tabel (`film` dan tabel turunan). Karena `id_film` dibuat otomatis oleh database, DAO meminta ID tersebut kembali lewat `Statement.RETURN_GENERATED_KEYS`, lalu memakainya untuk insert ke tabel turunan:    

<img width="600" alt="image" src="https://github.com/user-attachments/assets/58a63cde-9917-4e99-838f-f69310ea5df3" />

### 6. Struktur Tabel
Berdasarkan query yang dipakai di DAO, database `db_film_review` terdiri dari empat tabel:

| Tabel | Kolom yang dipakai | Keterangan |
|---|---|---|
| `film` | `id_film`, `judul`, `sutradara`, `tahun_rilis`, `genre`, `durasi`, `tipe_film` | Data umum semua film |
| `feature_film` | `id_film`, `pemeran_utama`, `karakter_live_action` | Data khas feature film |
| `animated_film` | `id_film`, `pengisi_suara`, `karakter_animasi`, `gaya_animasi` | Data khas animated film |
| `review` | `id_review`, `id_film`, `rating`, `ulasan`, `is_rewatch` | Review tiap film |

### 7. Cara Menjalankan
1. Aktifkan **MySQL** (misalnya lewat XAMPP) dan pastikan JDK terpasang.
2. Buat database kosong bernama `db_film_review` di phpMyAdmin, lalu **import** file `db_film_review.sql` (berisi struktur tabel dan data dummy).
3. Sesuaikan `URL`, `USER`, dan `PASS` di `config/Koneksi.java` jika berbeda dengan pengaturan MySQL kamu.
4. Jalankan `config/TestKoneksi.java`. Jika berhasil akan muncul pesan koneksi berhasil.
5. Jalankan `main/Main.java`. Jika MySQL belum aktif, program menampilkan pesan bahwa database belum terhubung lalu berhenti.

---

## Demonstrasi Program ─ ★ ˙ ̟ !!

### 1. Menu Utama

  <img width="481" height="162" alt="image" src="https://github.com/user-attachments/assets/83f6a1b2-690d-4e49-9540-6b3b12520f1a" />

### 2. Tambah Film
- **Berhasil**
  
  <img width="476" height="333" alt="image" src="https://github.com/user-attachments/assets/e4670fdc-30e9-4dfa-88f9-b9e8bbcf798d" />

  Hasil Input Film:
  
  <img width="474" height="491" alt="image" src="https://github.com/user-attachments/assets/07ace25d-9f58-4feb-973f-57845f0eb114" />

- **Uji validasi**
  - Judul kosong: Input tidak boleh kosong!
    
    <img width="469" height="36" alt="image" src="https://github.com/user-attachments/assets/dab01e64-c264-423c-8848-e921b127f0e7" />

  - Tahun rilis di masa depan atau 0: Tahun rilis harus antara 1888 hingga tahun 2026.
  
    <img width="478" height="40" alt="image" src="https://github.com/user-attachments/assets/b969e614-aea6-4fde-8acc-501311b6fe9f" />

  - Durasi negatif: Durasi film harus minimal 30 menit.
  
    <img width="470" height="36" alt="image" src="https://github.com/user-attachments/assets/699b2e3f-4c54-481c-9f8c-3e99ce1d5fa9" />

  - Pilihan menu tidak valid: Pilihan jenis film tidak valid!
  
    <img width="472" height="140" alt="image" src="https://github.com/user-attachments/assets/85eb0d13-b10e-4b6d-81ce-4fd7aee66a52" />


### 3. Lihat Daftar Film  

<img width="474" height="530" alt="image" src="https://github.com/user-attachments/assets/392797d1-57f1-4a34-909c-e8c3796e8681" />  


Menampilkan seluruh film yang tersimpan beserta review-nya. Film bertipe `FeatureFilm` dan `AnimatedFilm` tampil dalam satu daftar, tetapi detail spesifiknya berbeda. Ini bukti **polymorphism** dari `tampilkanDetailSpesifik()`.

### 4. Update Review  

  <img width="476" height="516" alt="image" src="https://github.com/user-attachments/assets/21f80d11-a489-430f-9f9a-8010449681ec" />  

Mengubah review yang sudah ada pada sebuah film. User memilih review yang ingin diubah melalui ID Review, lalu mengisi review baru.  

Hasil Update Review:  

  <img width="473" height="357" alt="image" src="https://github.com/user-attachments/assets/0c2054b4-6070-4b21-9597-a7ef9d720ff1" />  

Dapat terlihat pada gambar di atas bahwa review pada film menampilkan data terbaru yang telah di-update.  


### 5. Hapus Review  

<img width="476" height="473" alt="image" src="https://github.com/user-attachments/assets/f54c0e76-9a61-4b41-8ea9-79391f57578c" />  

Menghapus review dari sebuah film. User memilih review yang akan dihapus melalui ID Review.

Hasil Hapus Review:  
<img width="480" height="324" alt="image" src="https://github.com/user-attachments/assets/0fc85722-fa90-402d-9571-17489d22435c" />  

Dapat terlihat pada gambar di atas bahwa review yang dipilih untuk dihapus sudah tidak muncul pada daftar.

### 6. Keluar Program  
<img width="514" height="221" alt="image" src="https://github.com/user-attachments/assets/d0aed973-79df-4c55-91e9-80e8d9e6930d" />  

Memilih menu keluar menghentikan program dan menampilkan pesan penutup.  

## Kesimpulan ─ ★ ˙ ̟ !!

Sistem Tracker & Review Film berhasil dibangun sebagai aplikasi CLI berbasis Java dengan arsitektur **MVC + DAO** dan penyimpanan data di **MySQL** lewat **JDBC**. Pemisahan peran antara `model.entitas`, `model.dao`, `view`, dan `controller` membuat kode lebih rapi, mudah dibaca, dan mudah dikembangkan, karena setiap bagian punya tanggung jawab yang jelas. Seluruh fitur CRUD berjalan dengan baik: menambah film, menambah review, melihat film beserta review, mengubah review, dan menghapus review.
 
Dari sisi konsep PBO, program ini menerapkan:
- **Encapsulation**, lewat atribut `protected` dan `private`, getter/setter berisi validasi, serta penggunaan keyword `final` agar data penting tidak berubah sembarangan.
- **Inheritance**, lewat class `Film` yang diwariskan ke `FeatureFilm` dan `AnimatedFilm`, sehingga kode umum cukup ditulis satu kali.
- **Abstraction**, lewat `Film` sebagai abstract class dengan abstract method `tampilkanDetailSpesifik()`.
- **Polymorphism**, lewat overriding `tampilkanDetailSpesifik()` pada tiap subclass dan overloading `tampilkanInfo()` pada `Film` dan `Review`.
- **Interface** `Displayable` sebagai nilai tambah, yang membuat `Film` dan `Review` punya cara menampilkan data yang seragam.
- **JDBC**, lewat class `Koneksi` yang membuat satu koneksi bersama dan menutupnya saat keluar, pola DAO, `PreparedStatement`, `ResultSet`.
  
Validasi input yang dilakukan oleh `ValidasiInput` dan setter di dalam model juga membuat data yang tersimpan tetap konsisten, sehingga program tidak mudah error akibat input yang salah. Pengecekan koneksi di awal program juga membuat aplikasi tidak langsung error ketika database belum aktif. Dengan begitu, program ini memenuhi ketentuan Minpro 4 dan menjadi dasar yang baik untuk dikembangkan lebih lanjut.

## Penutup ─ ★ ˙ ̟ !!
 
Ini adalah mini project terakhir di praktikum PBO. Terima kasih kepada asisten praktikum yang sudah membimbing, memberi revisi, dan menemani proses belajar dari Mini Project pertama sampai sekarang. Dari yang awalnya masih bingung bedanya `abstract` dan `interface`, sampai akhirnya bisa menghubungkan program Java ke database.
 
Semoga ilmu ini bisa terus dipakai dan dikembangkan di project berikutnya.
 
Sampai jumpa dan terima kasih! 🎬🍿
 
*- Atha Rina Sarwahita*
---

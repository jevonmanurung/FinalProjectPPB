# DermaScan

**DermaScan** adalah aplikasi Android berbasis deteksi gambar yang bertujuan untuk membantu pengguna mengenali jenis penyakit kulit secara cepat melalui pemindaian gambar. Aplikasi ini memanfaatkan kamera atau galeri untuk mengambil gambar, kemudian mengirimkannya ke model machine learning yang telah dilatih menggunakan Roboflow untuk memberikan prediksi kondisi kulit.

##  Fitur Utama

-  **Scan Penyakit Kulit melalui Kamera**
  - Ambil gambar langsung dari kamera untuk mendeteksi penyakit kulit.
  
-  **Unggah Gambar dari Galeri**
  - Pilih gambar dari penyimpanan perangkat untuk analisis.

-  **Riwayat Scan**
  - Menyimpan riwayat hasil deteksi yang telah dilakukan sebelumnya.

-  **Halaman Treatment**
  - Menyediakan informasi perawatan awal dari berbagai penyakit kulit umum, seperti jerawat, bisul, psoriasis, dan lainnya.

-  **Onboarding Interaktif**
  - Panduan awal penggunaan aplikasi yang ramah pengguna.

##  Teknologi yang Digunakan

- Android Studio (Java)
- Room Database untuk menyimpan riwayat
- Roboflow untuk training dan hosting model deteksi
- ConstraintLayout & Material Components untuk UI/UX
- Retrofit (opsional, jika menggunakan model REST)



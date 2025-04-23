# 🧱 Repomanager

Bu proje, `.rep` formatındaki dosyaları ve bunlara ait metadata içeren `meta.json` bilgilerini saklayabileceğin bir **package repository** servisidir.

Dosyalar ister yerel dosya sisteminde (`file-system`), ister MinIO üzerinde (`object-storage`) saklanabilir.

Spring Boot ile geliştirilmiştir. PostgreSQL kullanır, REST API destekler.

---

## 🚀 Başlarken

### 📦 Gereksinimler

- Java 21
- Maven
- Docker + Docker Compose

---

### 🔧 Projeyi Derleme

```bash
mvn clean package -DskipTests
```

---

## 🐳 Docker ile Başlatma

Projeyi Docker ile tamamen ayağa kaldırmak için:

```bash
docker-compose up --build
```

---

## 📌 Servisler

| Servis      | Açıklama             | Port                                |
|-------------|----------------------|-------------------------------------|
| Spring Boot | REST API             | `http://localhost:8080`             |
| PostgreSQL  | Veritabanı           | `5432`                              |
| MinIO       | Object storage (ops) | `http://localhost:9000` (API) <br> `http://localhost:9001` (UI) |

---

## 🟡 MinIO Giriş Bilgileri

- Kullanıcı: `minio`  
- Şifre: `minio123`

---

## ⚙️ Konfigürasyon (`application.properties`)

```properties
storage.strategy=file-system
storage.filesystem.root=/data/storage

spring.datasource.url=jdbc:postgresql://db:5432/repodb
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 🔁 Eğer MinIO kullanmak istersen

```properties
storage.strategy=object-storage
storage.object.endpoint=http://minio:9000
storage.object.bucket=rep-packages
storage.object.access-key=minio
storage.object.secret-key=minio123
```

---

## 📮 API Kullanımı

### 📤 Paket Yükleme

```http
POST /{packageName}/{version}
Content-Type: multipart/form-data
```

**Form-data:**
- `file`: `test-package-1.0.0.rep`
- `metadata`: `meta.json`

---

## 🧪 cURL ile Örnek – Paket Yükleme

```bash
curl -X POST http://localhost:8080/test-package/1.0.0   -F "file=@test-package-1.0.0.rep"   -F "metadata=@meta.json"
```

---

## 📥 Paket Dosyası İndirme

```http
GET /{packageName}/{version}/{fileName}
```

---

## 🧪 cURL ile Örnek – Dosya İndirme

```bash
curl -O http://localhost:8080/test-package/1.0.0/test-package-1.0.0.rep
```

---

## 🧪 Test Ortamı

- `.rep` dosyaları Docker container içinde `/data/storage` klasöründe saklanır.
- PostgreSQL veritabanı adı: `repodb`
- MinIO desteği hazırdır ancak varsayılan olarak file-system kullanılır.
- `docker-compose.yml` ile tüm ortam tek komutla ayağa kaldırılır.

---

## 👤 Geliştirici

**Ümit Şahan**  
📫 [GitHub - umit-sahan](https://github.com/umit-sahan)

---

## 📝 Lisans

Bu proje, **Repsy Junior Fullstack Developer Assignment** kapsamında geliştirilmiştir.  
Sadece kişisel portfolyo, değerlendirme ve eğitim amaçlı kullanıma açıktır.

Ticari kullanımı, dağıtımı veya yeniden paylaşımı izin gerektirir.

Telif hakkı © 2025 Ümit Şahan. Tüm hakları saklıdır.

# Aviation Flight Path Engine

Bu proje, lokasyonlar ve ulaşım yolları (transportation) arasında en uygun uçuş rotalarını hesaplayan yüksek performanslı bir arama motorudur. Clean Architecture prensiplerine uygun olarak geliştirilmiştir.

## Ön Koşullar (Prerequisites)

Uygulamayı yerel ortamınızda çalıştırmadan önce aşağıdaki bileşenlerin yüklü ve yapılandırılmış olduğundan emin olun:

- **Java 21**: JDK 21 sürümü yüklü olmalıdır. `JAVA_HOME` ortam değişkeninin bu sürümü göstermesi önerilir.
- **Maven 3.8+**: Projenin derlenmesi ve paketlenmesi için gereklidir.
- **Docker & Docker Compose (V2+)**: Veritabanı ve önbellek servislerini (veya uygulamanın tamamını) ayağa kaldırmak için gereklidir.

### Versiyon Kontrolü
Mevcut sürümlerinizi aşağıdaki komutlarla kontrol edebilirsiniz:

```bash
java -version
mvn -version
docker compose version
```

## Teknolojiler

- **Java 21**: Modern dil özellikleri ve performans iyileştirmeleri.
- **Spring Boot 3.2.x**: Uygulama iskeleti.
- **PostgreSQL**: Kalıcı veri depolama.
- **Redis & Redisson**: Hızlı veri erişimi, graf veri yapısı önbellekleme ve dağıtık kilit yönetimi.
- **Liquibase**: Veritabanı versiyonlama ve migration yönetimi.
- **Docker & Docker Compose**: Konteynerize dağıtım ve kolay kurulum.
- **OpenAPI (Swagger)**: API dokümantasyonu.
- **JUnit 5 & Mockito**: Kapsamlı birim ve entegrasyon testleri.

## Kurulum ve Çalıştırma

### 1. Docker Compose ile Hızlı Başlangıç (Önerilen)

Tüm bağımlılıkları (PostgreSQL, Redis) ve uygulamayı tek bir komutla ayağa kaldırabilirsiniz:

```bash
docker-compose up -d --build
```

Uygulama ayağa kalktığında **8899** portu üzerinden hizmet verecektir.

### 2. Maven ile Yerel Çalıştırma

Bağımlı servisleri (DB ve Cache) docker üzerinden başlattıktan sonra uygulamayı yerel olarak çalıştırabilirsiniz:

```bash
mvn clean spring-boot:run -Dspring-boot.run.profiles=local
```

## API Dokümantasyonu

Uygulama ayağa kalktıktan sonra tüm API uç noktalarına, parametrelerine ve şemalarına Swagger arayüzü üzerinden erişebilirsiniz:

[Swagger UI (http://localhost:8899/swagger-ui/index.html)](http://localhost:8899/swagger-ui/index.html)

## Güvenlik ve Özel Yapılandırmalar

### Kullanıcı Bilgileri ve Yetkilendirme
Sistemde register işlemi bulunmamaktadır. Test ve kullanım için aşağıdaki hazır kullanıcı hesaplarını kullanabilirsiniz:

- **ADMIN**: Kullanıcı Adı: `admin` | Şifre: `admin123` (Tüm yetkiler)
- **AGENCY**: Kullanıcı Adı: `agency` | Şifre: `agency123` (Sadece listeleme/görüntüleme yetkileri)

### Yerel Profil (`local`)
Geliştirme sürecini kolaylaştırmak için `local` profili ile uygulama başlatıldığında:
- **Security (Spring Security)** devre dışı bırakılır (tüm isteklere izin verilir).
- **CORS** ayarları en gevşek (permissive) modda çalışır (tüm originlere izin verilir).
- JWT doğrulama filtreleri atlanır.

### Docker ve Prod Profilleri
Bu profillerde tam güvenlik aktiftir. İsteklerin `Authorization: Bearer <token>` başlığı ile yapılması gerekmektedir.
- Varsayılan roller: `ADMIN` ve `AGENCY`.
- `AGENCY` rolü sadece GET (listeleme ve detay) işlemlerini yapabilir.
- `ADMIN` rolü tüm CRUD işlemlerine yetkilidir.

### Loglama
- `local` profilinde standart konsol logları basılır.
- `docker` ve `prod` profillerinde loglar merkezi sistemlere uygun olarak **JSON** formatında basılmaktadır.

## Testlerin Çalıştırılması

Projedeki tüm birim ve entegrasyon testlerini çalıştırmak için:

```bash
mvn test
```

Testler h2 in-memory veritabanı ve mock yapıları kullanarak hızlı bir şekilde sonuçlanmaktadır.

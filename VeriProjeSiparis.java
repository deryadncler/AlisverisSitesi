package veriprojesiparis;

import java.util.*;

class SiparisAgaci {

    class Dugum {

        String UrunAdi;
        int siparisSayi;
        int yükseklik;
        Dugum sol, sağ;

        Dugum(String UrunAdi, int siparisSayi) {
            this.UrunAdi = UrunAdi;
            this.yükseklik = 1;
            this.siparisSayi = siparisSayi;
            this.sol = null;
            this.sağ = null;
        }

    }
    private Dugum kok;// düğümden alınan kök

    public SiparisAgaci() {
        this.kok = null;   // kök başlangıçta null olarak atanır
    }
// compareTo yerine bu metot kullanılacak

    private int StringKarsilastirma(String s1, String s2) {
        int minUzunluk = Math.min(s1.length(), s2.length());
        for (int i = 0; i < minUzunluk; i++) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            if (c1 != c2) {
                return c1 - c2;
            }

        }
        return s1.length() - s2.length();
    }

    private int yukseklik(Dugum dugum) {

        return dugum == null ? 0 : dugum.yükseklik; // düğüm null ise 0 değeri değilse yüksekliği döndür

    }

    private int dengeFaktörü(Dugum dugum) {
        // dugum null ise 0 döndürür denge faktörü sol sağ yükseklik farkıdır 0 değilse onu döndürür  
        return dugum == null ? 0 : yukseklik(dugum.sol) - yukseklik(dugum.sağ);
    }

    private Dugum sagaDöndür(Dugum y) {
        // y dönen düğüm , x y nin sol düğümü, ikinciCocuk x in sağ çocuğu
        Dugum x = y.sol;
        Dugum ikinciCocuk = x.sağ;
        x.sağ = y;// x yeni düğüm yapıldı, y x in sol çocuğu
        y.sol = ikinciCocuk; // xin sağ çocuğu y nin sol çocuğu oldu
        //yükseklik farkları kontrol ediliyor
        y.yükseklik = Math.max(yukseklik(y.sol), yukseklik(y.sağ) + 1);
        x.yükseklik = Math.max(yukseklik(x.sol), yukseklik(x.sağ) + 1);
        //yeni kökü döndür
        return x;
    }

    private Dugum solaDöndür(Dugum x) {
        // x dönen düğüm, y x in sağı, ikinciCocuk y nin sol çocuğu
        Dugum y = x.sağ;
        Dugum ikinciCocuk = y.sol;
        y.sol = x;  //y yeni kök, x y nin sol çocuğu
        x.sağ = ikinciCocuk;// y nin sol çocuğu x in sağ çocuğu olur
        // yükseklik farkları kontrol edilir
        x.yükseklik = Math.max(yukseklik(x.sol), yukseklik(x.sağ));
        y.yükseklik = Math.max(yukseklik(y.sol), yukseklik(y.sağ));
        //yeni kökü döndür
        return y;
    }

    //Ürün ekleme fonksiyonu
    // kullanıcıdan ürün adı ve miktarı alındığında çalışır 
    //sipariseklerec fonksiyonunu çağırır ve kökü ona atar
    public void SiparisEkle(String UrunAdi, int miktar) {
        kok = SiparisEkleRec(kok, UrunAdi, miktar);

    }

    private Dugum SiparisEkleRec(Dugum dugum, String UrunAdi, int miktar) {
        // dugum null ise yeni düğüm oluşturup döndürür
        if (dugum == null) {
            return new Dugum(UrunAdi, miktar);
        }
        //ilk ifte yeni düğüm alfabetik olarak daha önce gelirse sola ekler
        if (StringKarsilastirma(UrunAdi, dugum.UrunAdi) < 0) {
            dugum.sol = SiparisEkleRec(dugum.sol, UrunAdi, miktar);

        } //alfaabetik olarak sonra geldiği için sağa ekler
        else if (StringKarsilastirma(UrunAdi, dugum.UrunAdi) > 0) {
            dugum.sağ = SiparisEkleRec(dugum.sağ, UrunAdi, miktar);
        } // eğer aynı ürün bir kez daha girilirse yeni düğüm oluşturup ekler
        else {
            Dugum yeniDugum = new Dugum(UrunAdi, miktar);
            if (dugum.sağ == null) {
                dugum.sağ = yeniDugum;
            } else {
                dugum.sol = yeniDugum; // Sağ doluysa sola eklenir
            }
            return dugum;
        }

        //yükseklikleri günceller dugum için denge faktörü hesaplanır
        dugum.yükseklik = 1 + Math.max(yukseklik(dugum.sol), yukseklik(dugum.sağ));

        int denge = dengeFaktörü(dugum);
        //denge faktörü 1 den büyükse ve yeni ürün adı sol alt daldaki üründen önce gelirse sağa döndürme yapılır(ağırlık solda)
        if (denge > 1 && StringKarsilastirma(UrunAdi, dugum.sol.UrunAdi) < 0) {
            return sagaDöndür(dugum);
        }
        // denge faktörü -1 den küçükse ve yeni eklenen ürün adı sağ alt saldakinden sonra gelirse sola döndürme yapılır(ağırlık sağda)
        if (denge < -1 && StringKarsilastirma(UrunAdi, dugum.sağ.UrunAdi) > 0) {
            return solaDöndür(dugum);
        }
        // denge 1 den büyük yani ağırlık solda 
        // ürün adı sol alt çocuktan büyükse onun sağına yerleştirilmiştir (sağ-sol dengesizliği var)
        //önce sol çocuk sola daha sonra kök sağa döndürülmelidir

        if (denge > 1 && StringKarsilastirma(UrunAdi, dugum.sol.UrunAdi) > 0) {
            dugum.sol = solaDöndür(dugum.sol);
            return sagaDöndür(dugum);
        }
        // denge -1 den küçük yani ağırlık sağda
        //ürün adı sağ alt çocuktan önce gelirse soluna yerleştirilmiştir (sol-sağ dengesizliği)
        // önce sağ çocuk sağa sonra kök sola döndürülmeli
        if (denge < -1 && StringKarsilastirma(UrunAdi, dugum.sağ.UrunAdi) < 0) {
            dugum.sağ = sagaDöndür(dugum.sağ);
            return solaDöndür(dugum);
        }
        return dugum;

    }

    //sipariş silme işlemi
    // silinecek ürün adı ve miktarı alınır ve siparissilmerec de rekürsif olarak yapılır
    public void SiparisSilme(String UrunAdi, int miktar) {
        kok = SiparisSilmeRec(kok, UrunAdi, miktar);
    }

    private Dugum SiparisSilmeRec(Dugum dugum, String UrunAdi, int miktar) {
        if (dugum == null) {
            System.out.println("Silinecek urun bulunamadi");
            return null;
        }

        // ürünün küçükse solda büyükse sağda olacak şekilde silinecek düğüm aranır
        //eşitse bulundu ve silme işlemine devam edilir
        if (StringKarsilastirma(UrunAdi, dugum.UrunAdi) < 0) {
            dugum.sol = SiparisSilmeRec(dugum.sol, UrunAdi, miktar);

        } else if (StringKarsilastirma(UrunAdi, dugum.UrunAdi) > 0) {
            dugum.sağ = SiparisSilmeRec(dugum.sağ, UrunAdi, miktar);
        } //bu blokta silinecek ürün bulunduysa çalışılır
        //eğer sipariş sayısı silinecek miktardan fazlaysa sipariş sayısı o kadar azaltılır ve dugum değeri döndürülür
        else {
            if (dugum.siparisSayi > miktar) {
                dugum.siparisSayi -= miktar;
                return dugum;
            } // eğer küçük veya eşitse düğümün tamamen silinmesi gerekir
            // düğümün sol veya sağ alt ağacı var mı bakar
            // eğer tek alt ağaç varsa gecici değerinde tutar
            else {
                if ((dugum.sol == null) || (dugum.sağ == null)) {
                    Dugum gecici = (dugum != null) ? dugum.sol : dugum.sağ;

                    //eğer hiç alt ağacı yoksa düğüm tamamen silinir
                    if (gecici == null) {
                        gecici = dugum;
                        dugum = null;
                    } //eğer alt ağacı varsa gecicide tutulur
                    else {
                        dugum = gecici;
                    }
                } // eğer hem sol hem sağ alt ağacı varsa minDegerDugum ile sağ alt ağacın en küçük değeri bulunur
                // o düğüm silinecek düğümün yerine geçer
                else {
                    Dugum gecici = minDegerDugum(dugum.sağ);
                    dugum.UrunAdi = gecici.UrunAdi;
                    dugum.siparisSayi = gecici.siparisSayi;
                    dugum.sağ = SiparisSilmeRec(dugum.sağ, gecici.UrunAdi, gecici.siparisSayi);
                }
            }
        }

        if (dugum == null) {
            return null;

        }
        // yükseklik durumu güncellenir ve denge faktörü kontol edilir
        dugum.yükseklik = Math.max(yukseklik(dugum.sol), yukseklik(dugum.sağ)) + 1;

        int denge = dengeFaktörü(dugum);

        // dengeye göre dönüş işlemleri yapılır
        if (denge > 1 && dengeFaktörü(dugum.sol) >= 0) {
            return sagaDöndür(dugum);
        }

        if (denge > 1 && dengeFaktörü(dugum.sol) < 0) {
            dugum.sol = solaDöndür(dugum.sol);
            return sagaDöndür(dugum);
        }

        if (denge < -1 && dengeFaktörü(dugum.sağ) <= 0) {
            return solaDöndür(dugum);
        }

        if (denge < -1 && dengeFaktörü(dugum.sağ) > 0) {
            dugum.sağ = sagaDöndür(dugum.sağ);
            return solaDöndür(dugum);
        }

        return dugum;

    }
    // ağaçtaki min değeri bulan fonksiyon (sol null olana kadar aramaya devam eder)

    private Dugum minDegerDugum(Dugum dugum) {
        while (dugum.sol != null) {
            dugum = dugum.sol;
        }
        return dugum;
    }

    // sipariş sayısı sorgulama
    public int SiparisSorgulama(String UrunAdi) {
        return SiparisSorgulamaRec(kok, UrunAdi);
    }

    private int SiparisSorgulamaRec(Dugum dugum, String UrunAdi) {
        // null ise ürün yoktur 0 döndür
        if (dugum == null) {
            return 0;

        }
        // ürün soldaysa arama soldan sağdaysa sağdan devam eder
        if (StringKarsilastirma(UrunAdi, dugum.UrunAdi) < 0) {
            return SiparisSorgulamaRec(dugum.sol, UrunAdi);
        } else if (StringKarsilastirma(UrunAdi, dugum.UrunAdi) > 0) {
            return SiparisSorgulamaRec(dugum.sağ, UrunAdi);
        } // ürün bulunmuşsa sipariş sayısı döndürülür
        else {
            return dugum.siparisSayi;
        }
    }

    // ürün kümesini sorgulama
    public int UrunKumesiSorgula(List<String> ürünler) {
        if (ürünler.isEmpty()) {
            return 0;
        }
        // collection.sort kullanmadan alfabetik sıralama
        for (int i = 0; i < ürünler.size() - 1; i++) {
            for (int j = 0; j < ürünler.size() - i - 1; j++) {
                if (StringKarsilastirma(ürünler.get(j), ürünler.get(j + 1)) > 0) {
                    //iki elemanın yerini değiştir
                    String gecici = ürünler.get(j);
                    ürünler.set(j, ürünler.get(j + 1));
                    ürünler.set(j + 1, gecici);
                }
            }
        }
        // arama işlemi için hedef olacak o yüzden en son düğüm bulunur
        String enSonUrun = ürünler.get(ürünler.size() - 1);
        return urunKumesiSorgulaRec(kok, ürünler, enSonUrun);

    }

    // kökten başlayarak ürün kümesini rekürsif olarak sorgular
    private int urunKumesiSorgulaRec(Dugum dugum, List<String> ürünler, String enSonUrun) {
        if (dugum == null) {
            return 0;
        }
        int toplam = 0;
        // eğer şuanki düğüm en son düğümle eşleştiyse tumurunleryolda fonksiyonu ile o yolda olan diğer düğümlere de bakılır
        //eğer tüm ürünler o yoldaysa sipariş sayısı toplam değerine eklenir ve döndürülür
        if (dugum.UrunAdi.equals(enSonUrun)) {
            if (tumUrunlerYolda(dugum, ürünler)) {
                toplam += dugum.siparisSayi;
            }
        }
        // rekürsif olarak sol ve sağ alt ağaç gezilir ve hepsinin değeri döndürülür

        toplam += urunKumesiSorgulaRec(dugum.sol, ürünler, enSonUrun);
        toplam += urunKumesiSorgulaRec(dugum.sağ, ürünler, enSonUrun);

        return toplam;

    }

    private boolean tumUrunlerYolda(Dugum dugum, List<String> ürünler) {
        // ürünler listesindeki her ürün için yoldabul fonksiyonunu çağırır
        // eğer ürün yoksa false döndürür
        for (String urun : ürünler) {
            if (!yoldaBul(dugum, urun)) {
                return false;
            }
        }
        return true;

    }

    private boolean yoldaBul(Dugum dugum, String urun) {
        // null ise ürün bu yolda değl demektir
        if (dugum == null) {
            return false;
        }
        // hangi ağaçta aranacağı karşılaştırma yapılarak bakılır
        if (StringKarsilastirma(urun, dugum.UrunAdi) < 0) {
            return yoldaBul(dugum.sol, urun);
        } else if (StringKarsilastirma(urun, dugum.UrunAdi) > 0) {
            return yoldaBul(dugum.sağ, urun);
        } else {
            return true;
        }
    }

    // ağacı sıralı şekilde yazdırma fonksiyonu
    public void AgaciSiraliYazdir(Dugum dugum, String prefix, boolean isTail) {
        if (dugum != null) {
            System.out.println(prefix + (isTail ? "└── " : "├── ") + dugum.UrunAdi + "(" + dugum.siparisSayi + ")");
            // eğer düğümün sol veya sağ çocuğu varsa onları da yazdırır
            if (dugum.sol != null || dugum.sağ != null) {
                AgaciSiraliYazdir(dugum.sol, prefix + (isTail ? "    " : "│   "), dugum.sağ == null);
                AgaciSiraliYazdir(dugum.sağ, prefix + (isTail ? "    " : "│   "), true);
            }
        }

    }

    // ağaç içeriğini sıralı yazdırır
    public void agaciYazdir() {
        AgaciSiraliYazdir(kok, "", true);
    }

}

public class VeriProjeSiparis {

    public static void main(String[] args) {

        SiparisAgaci siparisagaci = new SiparisAgaci();
        Scanner klavye = new Scanner(System.in);

        while (true) {
            System.out.println("Siparis Kontrol Menusu: ");
            System.out.println("Islemi uygulamak ic1in uygun secimi yapiniz");
            System.out.println("1- Siparis Ekle \n2- Siparis Silme \n3-Siparis Sorgula \n4-Urun Kumesi Sorgula \n5-Agaci Yazdir \n6- Cikis ");

            int secim = klavye.nextInt();
            klavye.nextLine();

            switch (secim) {
                case 1:
                    System.out.println("Urun adi: ");
                    String urunadiEkle = klavye.nextLine();
                    System.out.println("Siparis Miktari: ");
                    int siparismiktarEkle = klavye.nextInt();

                    siparisagaci.SiparisEkle(urunadiEkle, siparismiktarEkle);
                    break;
                case 2:
                    System.out.println("Urun adi: ");
                    String urunSil = klavye.nextLine();
                    System.out.println("Silinecek miktar: ");
                    int miktarSil = klavye.nextInt();

                    siparisagaci.SiparisSilme(urunSil, miktarSil);
                    break;
                case 3:
                    System.out.println("Urun adi: ");
                    String sorgula = klavye.nextLine();
                    int miktar = siparisagaci.SiparisSorgulama(sorgula);
                    System.out.println(sorgula + " toplam siparis sayisi: " + miktar);
                    break;
                case 4:
                    System.out.println("Urunleri virgülle ayırarak giriniz ");
                    String urunKüme = klavye.nextLine();
                    List<String> urunler = Arrays.asList(urunKüme.split(","));
                    int toplamSiparis = siparisagaci.UrunKumesiSorgula(urunler);
                    System.out.println("Urun kümesi toplam siparis sayisi: " + toplamSiparis);
                    break;
                case 5:
                    System.out.println("Agac yapisi: ");
                    siparisagaci.agaciYazdir();
                    break;
                case 6:
                    System.out.println("Cikis yapmak istediniz, cikiliyor..");
                    klavye.close();
                    break;
                default:
                    System.out.println("Gecersiz secim yapildi.Lütfen tekrar deneyin.");
                    break;

            }

        }
    }

}

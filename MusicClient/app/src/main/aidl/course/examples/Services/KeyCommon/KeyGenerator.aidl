package course.examples.Services.KeyCommon;

interface KeyGenerator {
    List<String> getSong();
    List<String> getArtist();
    List<String> getURL();
    Bitmap getImage(int i);
}
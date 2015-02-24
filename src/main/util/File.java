package util;

public class File {
    private String file;
    private final static String MIAS = "mias/MIAS.txt";
    private final static String DDSM = "ddsm/DDSM.txt";
    private final static String DDSM_SHORT = "ddsm_short/DDSMShort.txt";
    private final static String BCDR_FILM = "bcdr_film/BCDRFilm.txt";
    private final static String BCDR_DIGITAL = "bcdr/BCDRDigital.txt";

    public File(String fileCode) {
        if (fileCode.equals("mias"))
            file = MIAS;
        else if (fileCode.equals("ddsm")) {
            file = DDSM;
        } else if (fileCode.equals("bcdr_film")) {
            file = BCDR_FILM;
        } else if (fileCode.equals("bcdr_dig")) {
            file = BCDR_DIGITAL;
        } else if (fileCode.equals("ddsm_short")){
            file = DDSM_SHORT;
        }else{
            System.err.println("Could not set file: invalid file code!");
        }
    }

    public String getMomentsFile() {
        return file.split("\\.")[0] + "Moments.txt";
    }

    public String getHaralickFile() {
        return file.split("\\.")[0] + "Haralick.txt";
    }

    public String getGLRLFile() {
        return file.split("\\.")[0] + "GLRL.txt";
    }


    public String getGaborFile() {
        return file.split("\\.")[0] + "Gabor.txt";
    }

    public String getHOGFile() {
        return file.split("\\.")[0] + "HOG.txt";
    }

    public String getCSSFile() {
        return file.split("\\.")[0] + "CSS.txt";
    }

    public String getFile() {
        return file;
    }
}

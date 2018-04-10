package org.tinymediamanager.core.movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.tinymediamanager.BasicTest;
import org.tinymediamanager.core.MediaFileType;
import org.tinymediamanager.core.Settings;
import org.tinymediamanager.core.entities.MediaFile;
import org.tinymediamanager.core.entities.MediaFileAudioStream;
import org.tinymediamanager.core.movie.entities.Movie;

public class MovieMediaFileTest extends BasicTest {

  @BeforeClass
  public static void beforeClass() {
    Settings.getInstance(getSettingsFolder());
  }

  @Test
  public void renameSameNameOtherCase() throws IOException {
    MediaFile mf1 = new MediaFile(Paths.get("./movie.mkv"));
    MediaFile mf2 = new MediaFile(Paths.get("./Movie.mkv"));
    System.out.println("MF1: " + mf1.getFileAsPath());
    System.out.println("MF2: " + mf2.getFileAsPath());

    // true on Windows, comparing "Path" object
    System.out.println("Is same file? " + Files.isSameFile(mf1.getFileAsPath(), mf2.getFileAsPath()));
    System.out.println("Is equals() ? " + mf1.equals(mf2));

    ArrayList<MediaFile> cleanup = new ArrayList<>();
    ArrayList<MediaFile> needed = new ArrayList<>();
    cleanup.add(mf1);
    needed.add(mf2);

    // contains also done via "Path" impl
    if (!needed.contains(cleanup.get(0))) {
      MediaFile cl = cleanup.get(0);
      System.out.println("MF2 in list, but '" + cl.getFileAsPath() + "' not in there");

      if (Files.exists(cl.getFileAsPath())) {
        // mac/linux
        // TBD
      }
    }
    else {
      // WIN impl says, both are same, so
      System.out.println("MF1 == MF2 - no cleanup needed");
    }
  }

  @Test
  public void testUpdateMediaFilePath() {
    Movie movie = new Movie();
    movie.setPath("/private/Test_Filme/Alien Collecion/Alien 1");
    Path mediaFile = Paths.get("/private/Test_Filme/Alien Collecion/Alien 1/asdf/jklö/VIDEO_TS/VIDEO_TS.IFO");
    MediaFile mf = new MediaFile(mediaFile);
    movie.addToMediaFiles(mf);

    System.out.println("Movie Path: " + movie.getPathNIO());
    System.out.println("File Path:  " + movie.getMediaFiles().get(0).getFileAsPath());

    Path oldPath = movie.getPathNIO();
    Path newPath = Paths.get("/private/Test_Filme/Alien 1");
    movie.updateMediaFilePath(oldPath, newPath);
    movie.setPath(newPath.toAbsolutePath().toString());

    System.out.println("Movie Path: " + movie.getPathNIO());
    System.out.println("File Path:  " + movie.getMediaFiles().get(0).getFileAsPath());
  }

  @Test
  public void filenameWithoutStacking() {
    MediaFile mf = new MediaFile(Paths.get(".", "hp7 - part 1"));
    System.out.println(mf.getFilenameWithoutStacking()); // not stacked
    mf.setStacking(1);
    mf.setStackingMarker("part 1");
    System.out.println(mf.getFilenameWithoutStacking()); // stacked
  }

  @Test
  public void ExtrasTest() {
    // video
    MediaFileType mft = MediaFileType.VIDEO;
    checkExtra("E.T. el extraterrestre", mft);
    checkExtra("E.T. the Extra-Terrestrial", mft);
    checkExtra("Extra", mft);
    checkExtra("Extras", mft);
    checkExtra("Extra 2012", mft);
    checkExtra("Extras", mft);
    checkExtra("LazyTown Extra", mft);
    checkExtra("Extra! Extra!", mft);
    checkExtra("Extra.Das.RTL.Magazin.2014-06-02.GERMAN.Doku.WS.dTV.x264", mft);
    checkExtra("Person.of.Interest.S02E14.Extravaganzen.German.DL.720p.BluRay.x264", mft);
    checkExtra("The.Client.List.S02E04.Extra.gefaellig.GERMAN.DUBBED.DL.720p.WebHD.h264", mft);
    checkExtra("The.Amazing.World.of.Gumball.S03E06.The.Extras.720p.HDTV.x264", mft);
    checkExtra("", mft);

    // video_extra
    mft = MediaFileType.VIDEO_EXTRA;
    checkExtra("Red.Shoe.Diaries.S01.EXTRAS.DVDRip.X264", mft);
    checkExtra("Extra/extras/some-trailer", mft);
    checkExtra("extras/someExtForSomeMovie-trailer", mft);
    checkExtra("extras/someExtForSomeMovie", mft);
    checkExtra("extra/The.Amazing.World.of.Gumball.S03E06.720p.HDTV.x264", mft);
    checkExtra("bla-blubb-extra", mft);
    checkExtra("bla-blubb-extra-something", mft);
    checkExtra("bla-blubb-extra-", mft);
    checkExtra("", mft);

    System.out.println("All fine :)");
  }

  private void checkExtra(String filename, MediaFileType mft) {
    if (filename.isEmpty()) {
      return;
    }
    Path f = Paths.get(".", filename + ".avi");
    System.out.print("testing " + f + " for ");
    MediaFile mf = new MediaFile(f);
    assertEqual(mft, mf.getType());
  }

  @Test
  public void testAudioChannels() {
    MediaFileAudioStream as = new MediaFileAudioStream();
    as.setChannels("");
    assertEqual(0, as.getChannelsAsInt());
    as.setChannels("4");
    assertEqual(4, as.getChannelsAsInt());
    as.setChannels("5.1");
    assertEqual(6, as.getChannelsAsInt());
    as.setChannels("5.1channels");
    assertEqual(6, as.getChannelsAsInt());
    as.setChannels("8 / 6");
    assertEqual(8, as.getChannelsAsInt());
    as.setChannels("8 / 6 Ch");
    assertEqual(8, as.getChannelsAsInt());

    as.setChannels("4 / 5.2 / 8 / 6 / 7.3.1 / 9");
    assertEqual(11, as.getChannelsAsInt());

    as.setChannels("Object Based / 8 channels");
    assertEqual(8, as.getChannelsAsInt());

    as.setChannels("11 objects / 6 channels");
    assertEqual(6, as.getChannelsAsInt());
    as.setChannels("11 objects / 5.1 channels");
    assertEqual(6, as.getChannelsAsInt());

  }
}

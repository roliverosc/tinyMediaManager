/*
 * Copyright 2012 - 2017 Manuel Laggner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinymediamanager.ui.moviesets;

import java.awt.FontMetrics;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.tinymediamanager.core.MediaFileType;
import org.tinymediamanager.core.entities.MediaEntity;
import org.tinymediamanager.core.entities.MediaFile;
import org.tinymediamanager.core.entities.Rating;
import org.tinymediamanager.core.movie.entities.Movie;
import org.tinymediamanager.core.movie.entities.MovieSet;
import org.tinymediamanager.ui.IconManager;
import org.tinymediamanager.ui.UTF8Control;
import org.tinymediamanager.ui.components.table.TmmTableFormat;
import org.tinymediamanager.ui.components.tree.TmmTreeNode;

/**
 * The class MovieSetTableFormat is used to define the columns for the movie set tree table
 *
 * @author Manuel Laggner
 */
public class MovieSetTableFormat extends TmmTableFormat<TmmTreeNode> {
  private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages", new UTF8Control());

  public MovieSetTableFormat() {

    FontMetrics fontMetrics = getFontMetrics();

    /*
     * movie count
     */
    Column col = new Column(BUNDLE.getString("movieset.moviecount"), "seasons", node -> getMovieCount(node), String.class);
    col.setHeaderIcon(IconManager.COUNT);
    col.setColumnResizeable(false);
    col.setMinWidth((int) (fontMetrics.stringWidth("99") * 1.2f));
    addColumn(col);

    /*
     * rating
     */
    col = new Column(BUNDLE.getString("metatag.rating"), "rating", node -> getRating(node), String.class);
    col.setHeaderIcon(IconManager.RATING);
    col.setColumnResizeable(false);
    col.setMinWidth((int) (fontMetrics.stringWidth("99.9") * 1.2f));
    addColumn(col);

    /*
     * video format
     */
    col = new Column(BUNDLE.getString("metatag.format"), "format", node -> getFormat(node), String.class);
    col.setHeaderIcon(IconManager.VIDEO_FORMAT);
    col.setColumnResizeable(false);
    col.setMinWidth((int) (fontMetrics.stringWidth("1080p") * 1.2f));
    addColumn(col);

    /*
     * main video file size
     */
    col = new Column(BUNDLE.getString("metatag.size"), "fileSize", node -> getFileSize(node), String.class);
    col.setHeaderIcon(IconManager.FILE_SIZE);
    col.setColumnResizeable(false);
    col.setMinWidth((int) (fontMetrics.stringWidth("50000M") * 1.2f));
    addColumn(col);

    /*
     * NFO
     */
    col = new Column(BUNDLE.getString("tmm.nfo"), "nfo", node -> hasNfo(node), ImageIcon.class);
    col.setHeaderIcon(IconManager.NFO);
    col.setColumnResizeable(false);
    addColumn(col);

    /*
     * images
     */
    col = new Column(BUNDLE.getString("tmm.images"), "images", node -> hasImages(node), ImageIcon.class);
    col.setHeaderIcon(IconManager.IMAGES);
    col.setColumnResizeable(false);
    addColumn(col);

    /*
     * watched
     */
    col = new Column(BUNDLE.getString("metatag.watched"), "watched", node -> isWatched(node), ImageIcon.class);
    col.setHeaderIcon(IconManager.WATCHED);
    col.setColumnResizeable(false);
    addColumn(col);
  }

  private String getMovieCount(TmmTreeNode node) {
    Object userObject = node.getUserObject();
    if (userObject instanceof MovieSet) {
      int size = ((MovieSet) userObject).getMovies().size();
      if (size > 0) {
        return String.valueOf(size);
      }
    }
    return "";
  }

  private String getRating(TmmTreeNode node) {
    Object userObject = node.getUserObject();
    if (userObject instanceof Movie) {
      Rating rating = ((MediaEntity) userObject).getRating();
      if (rating != null && rating.getRating() > 0) {
        return String.valueOf(rating.getRating());
      }
    }
    return "";
  }

  private String getFormat(TmmTreeNode node) {
    Object userObject = node.getUserObject();
    if (userObject instanceof Movie) {
      return ((Movie) userObject).getMediaInfoVideoFormat();
    }
    return "";
  }

  private String getFileSize(TmmTreeNode node) {
    Object userObject = node.getUserObject();
    if (userObject instanceof Movie) {
      long size = 0;
      for (MediaFile mf : ((Movie) userObject).getMediaFiles(MediaFileType.VIDEO)) {
        size += mf.getFilesize();
      }

      return (int) (size / (1024.0 * 1024.0)) + " M";
    }
    return "";
  }

  private ImageIcon hasNfo(TmmTreeNode node) {
    Object userObject = node.getUserObject();
    if (userObject instanceof Movie) {
      return getCheckIcon(((Movie) userObject).getHasNfoFile());
    }
    return null;
  }

  private ImageIcon hasImages(TmmTreeNode node) {
    Object userObject = node.getUserObject();
    if (userObject instanceof MovieSet) {
      return getCheckIcon(((MovieSet) userObject).getHasImages());
    }
    if (userObject instanceof Movie) {
      return getCheckIcon(((Movie) userObject).getHasImages());
    }
    return null;
  }

  private ImageIcon isWatched(TmmTreeNode node) {
    Object userObject = node.getUserObject();
    if (userObject instanceof MovieSet) {
      return getCheckIcon(((MovieSet) userObject).isWatched());
    }
    if (userObject instanceof Movie) {
      return getCheckIcon(((Movie) userObject).isWatched());
    }
    return null;
  }
}

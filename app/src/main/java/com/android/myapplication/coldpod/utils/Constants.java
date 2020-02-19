package com.android.myapplication.coldpod.utils;

public class Constants {
    /*BaseUrl for iTunes Store to get podcasts*/
    /*Example: https://rss.itunes.apple.com/api/v1/us/podcasts/top-podcasts/all/25/explicit.json*/
    public static final String BASE_URL = "https://rss.itunes.apple.com/api/v1/";
    /** URL for a lookup request */
    /*Example: https://itunes.apple.com/lookup?id=1465334342*/
    public static final String I_TUNES_LOOKUP = "https://itunes.apple.com/lookup";

    public static final String EXTRA_PODCAST_ID = "extra_podCastId";
    public static final String EXTRA_PODCAST_NAME="extra_podcastName";

    public static final String DATABASE_NAME = "podcast";

    public static final String IMG_HTML_TAG = "<img.+?>";
    public static final String REPLACEMENT_EMPTY = "";

    public static final int GRID_AUTO_FIT_COLUMN_WIDTH = 380;
    public static final int GRID_COLUMN_WIDTH_DEFAULT = 48;
    public static final int GRID_SPAN_COUNT = 1;
}

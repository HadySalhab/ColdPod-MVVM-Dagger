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
    public static final String EXTRA_ITEM = "extra_item";
    public static final String ACTION_RELEASE_OLD_PLAYER = "action_release_old_player";

    public static final String DATABASE_NAME = "podcast";

    public static final String IMG_HTML_TAG = "<img.+?>";
    public static final String REPLACEMENT_EMPTY = "";

    public static final int GRID_AUTO_FIT_COLUMN_WIDTH = 380;
    public static final int GRID_COLUMN_WIDTH_DEFAULT = 48;
    public static final int GRID_SPAN_COUNT = 1;

    public static final String DELETE = "Delete";
    public static final int GROUP_ID_DELETE = 0;
    public static final int ORDER_DELETE = 0;

    /** Notification channel */
    public static final String PLAYBACK_CHANNEL_ID = "cold_pod_playback_channel";
    public static final int PLAYBACK_NOTIFICATION_ID = 1;

    /** The fast forward increment and rewind increment (milliseconds) */
    public static final int FAST_FORWARD_INCREMENT = 30000; // 30 sec
    public static final int REWIND_INCREMENT = 10000; // 10 sec

    /** The pending intent id is used to uniquely reference the pending intent */
    public static final int NOTIFICATION_PENDING_INTENT_ID = 0;

    public static final String EXTRA_PODCAST_IMAGE ="extra_podcastImage" ;

    /** URL for search request */
    public static final String I_TUNES_SEARCH = "https://itunes.apple.com/search";

    /** The parameter value for a search field. The  "podcast" is the media type to search. */
    public static final String SEARCH_MEDIA_PODCAST = "podcast";


    /** The pubDate pattern */
    public static final String PUB_DATE_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";
    /** The formatted date pattern */
    public static final String FORMATTED_PATTERN = "MMM d, yyyy";

    public static final String SHARE_INTENT_TYPE_TEXT = "text/plain";

    public static final String TYPE_AUDIO = "audio/mpeg";

    public static final String STATE_SEARCH_QUERY = "state_search_query";
}

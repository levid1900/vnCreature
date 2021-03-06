package com.vncreatures.common;

import com.vncreatures.R;

public class Common {
	public static final int THEME = R.style.Theme_Styled_NoTitleBar_Fullscreen;
	public static final int COMMON_ID = 1000;
	
	public static final String CREATURE_EXTRA = "creatureId";
	public static final String ACTION_EXTRA = "view detail";
	public static final String CAT_EXTRA = "categoryId";
	public static final String NEWS_EXTRA = "newsId";
	public static final String PARK_EXTRA = "parkId";
	public static final String ACTION_CHOOSE_FAMILY = "choose family";
	public static final String ACTION_CHOOSE_CLASS = "choose class";
	public static final String ACTION_CHOOSE_ORDER = "choose order";

	public static final String FAMILY_EXTRA = "familyId";
	public static final String ORDER_EXTRA = "orderId";
	public static final String CLASS_EXTRA = "classId";

	public static final String KINGDOM = "kingdom";

	public static final int CREATURE_ACTIVITY_REQUEST_CODE = 0;
	public static final int SELECT_PICTURE = 1;
	public static final String CREATURE_URL_IMAGES_EXTRA = "urlImages";
	public static final String CREATURE_URL_IMAGES_LIST = "urlImagesList";
	public static final String CREATURE_URL_IMAGES_POSITION = "urlImagesPosition";
	
	public static final String KINGDOM_PREF = "KingdomPrefs";
	public static final String TAB_PREF = "tabPrefs";
	public static final String SUGGEST_TITLE_PREF = "titlePrefs";
	
	public static final String USER_ID = "userid";
	public static final String USER_NAME = "username";
	public static final String FB_ID = "fbid";
	public static final String THREAD_ID = "threadid";
	public static final String CURRENT_TIME = "timestamp";
	
	//Validation message
	public static final String TITLE_MESSAGE = "Tiêu đề không được để trống";
	public static final String CONTENT_MESSAGE = "Nội dung không được để trống";
	public static final String MINLENGTH_MESSAGE = "Nội dung không được ít hơn 8 ký tự";

	public enum CREATURE {
		animal("1"), plant("2"), insect("3");

		private final String identifier;

		private CREATURE(String identifier) {
			this.identifier = identifier;
		}

		public String toString() {
			return identifier;
		}

		public static String getEnumNameForValue(Object value) {
			CREATURE[] values = CREATURE.values();
			String enumValue = null;
			for (CREATURE eachValue : values) {
				enumValue = eachValue.toString();
				if (enumValue.equals(value)) {
					return eachValue.name();
				}
			}
			return enumValue;
		}
	}
}
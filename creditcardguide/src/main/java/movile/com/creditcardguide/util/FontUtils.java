package movile.com.creditcardguide.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontUtils {

    private static Typeface tfDefaultThin;
    private static Typeface tfDefaultThinItalic;
    private static Typeface tfDefaultLight;
    private static Typeface tfDefaultLightItalic;
    private static Typeface tfDefaultRegular;
    private static Typeface tfDefaultMedium;
    private static Typeface tfDefaultMediumItalic;
    private static Typeface tfDefaultBold;
    private static Typeface tfDefaultBoldItalic;
    private static Typeface tfDefaultItalic;
    private static Typeface tfDefaultOCR;

    private FontUtils(){}

    // Returns/Load the app's default font
    private static Typeface getDefaultFont(Context context, FontType type) {
        if (tfDefaultRegular == null) {
            tfDefaultRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
            tfDefaultThin = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
            tfDefaultThinItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-ThinItalic.ttf");
            tfDefaultLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
            tfDefaultLightItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-LightItalic.ttf");
            tfDefaultMedium = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
            tfDefaultMediumItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-MediumItalic.ttf");
            tfDefaultBold = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
            tfDefaultBoldItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-BoldItalic.ttf");
            tfDefaultItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Italic.ttf");
            tfDefaultOCR = Typeface.createFromAsset(context.getAssets(), "fonts/OCRAStd.otf");
        }

        Typeface typeface;

        switch (type) {
            case OCR:
                typeface = tfDefaultOCR;
                break;

            case THIN:
                typeface = tfDefaultThin;
                break;

            case THIN_ITALIC:
                typeface = tfDefaultThinItalic;
                break;
            case LIGHT:
                typeface = tfDefaultLight;
                break;

            case LIGHT_ITALIC:
                typeface = tfDefaultLightItalic;
                break;

            case BOLD:
                typeface = tfDefaultBold;
                break;

            case BOLD_ITALIC:
                typeface = tfDefaultBoldItalic;
                break;

            case MEDIUM:
                typeface = tfDefaultMedium;
                break;

            case MEDIUM_ITALIC:
                typeface = tfDefaultMediumItalic;
                break;

            case ITALIC:
                typeface = tfDefaultItalic;
                break;

            case REGULAR:
                typeface = tfDefaultRegular;
                break;

            default:
                typeface = tfDefaultRegular;
                break;
        }

        return typeface;
    }

    // Set the default font for view and its children
    public static void loadFonts(View view) {
        if (view instanceof TextView) {
            // Set font to components
            ((TextView) view).setTypeface(getDefaultFont(view.getContext(), FontType.get(view.getTag())));
        } else if (view instanceof ViewGroup) {
            // Search for children to set font recursively
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
                loadFonts(((ViewGroup) view).getChildAt(i));
        }
    }

    public static void apply(View view, FontType font) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(getDefaultFont(view.getContext(), font));
        }
    }

    public enum FontType {
        THIN("t"), THIN_ITALIC("ti"), LIGHT("l"), LIGHT_ITALIC("li"), REGULAR("r"), MEDIUM("m"),
        MEDIUM_ITALIC("mi"), BOLD("b"), BOLD_ITALIC("bi"), ITALIC("i"), OCR("o");

        private String tag;

        private FontType(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }

        public static FontType get(Object tagObject) {
            String tag = "r";

            try {
                tag = (String) tagObject;
            } catch (Exception e) {

            }

            FontType fontType = REGULAR;

            for (FontType type : FontType.values()) {
                if (TextUtils.equals(type.getTag(), tag)) {
                    fontType = type;
                }
            }

            return fontType;
        }

    }
}

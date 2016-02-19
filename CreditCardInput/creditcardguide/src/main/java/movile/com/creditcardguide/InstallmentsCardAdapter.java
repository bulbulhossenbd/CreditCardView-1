package movile.com.creditcardguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import movile.com.creditcardguide.util.FontUtils;

/**
 * Created by FelipeSilvestre on 07/11/14.
 */
public class InstallmentsCardAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> portions;

    public InstallmentsCardAdapter(Context context, List<Integer> portions) {
        this.context = context;
        this.portions = portions;

        if (portions.isEmpty()) {
            portions.add(0);
        }

    }

    @Override
    public int getCount() {
        return portions.size();
    }

    @Override
    public Object getItem(int position) {
        return portions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_portion_card, null);
        }

        FontUtils.loadFonts(convertView);

        Integer portion = portions.get(position);

        if (portion > 0) {

            ((TextView) convertView.findViewById(R.id.adapter_portion_card_txt_name)).setText(String.format("%dx", portion));

        } else {

            ((TextView) convertView.findViewById(R.id.adapter_portion_card_txt_name)).setText("-");
            FontUtils.apply(convertView.findViewById(R.id.adapter_portion_card_txt_name), FontUtils.FontType.LIGHT);
            ((TextView) convertView.findViewById(R.id.adapter_portion_card_txt_name))
                    .setHintTextColor(context.getResources().getColor(R.color.gray_hint));

        }

        return convertView;
    }
}

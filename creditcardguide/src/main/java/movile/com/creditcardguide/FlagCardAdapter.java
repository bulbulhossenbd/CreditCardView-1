package movile.com.creditcardguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.model.PurchaseOption;
import movile.com.creditcardguide.util.FontUtils;

public class FlagCardAdapter extends BaseAdapter implements Serializable {

    private Context context;
    private List<PurchaseOption> purchaseOptions;

    public FlagCardAdapter(Context context, List<PurchaseOption> purchaseOptions) {
        this.context = context;
        this.purchaseOptions = purchaseOptions;
    }

    @Override
    public int getCount() {
        return purchaseOptions.size();
    }

    @Override
    public PurchaseOption getItem(int position) {
        return purchaseOptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_flag_card, null);
        }

        FontUtils.loadFonts(convertView);

        PurchaseOption option = purchaseOptions.get(position);
        IssuerCode issuerCode = option.getIssuerCode();
        ((ImageView) convertView.findViewById(R.id.adapter_flag_card_img_card)).setImageResource(issuerCode.getIconId());
        ((TextView) convertView.findViewById(R.id.adapter_flag_card_txt_name)).setText(issuerCode.getNameId());

        return convertView;
    }
}

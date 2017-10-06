package com.app.shoptree.shoptree.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.shoptree.shoptree.CategoryActivity;
import com.app.shoptree.shoptree.MyAddressActivity;
import com.app.shoptree.shoptree.R;
import com.app.shoptree.shoptree.model.Address;
import com.app.shoptree.shoptree.model.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lovishbajaj on 11/07/17.
 */

public class AddressAdapter extends ArrayAdapter {
    private Context mcontext;
    private ArrayList<Address> addresses;
    private static LayoutInflater inflater = null;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;
    int Resource;
    public AddressAdapter(Context context,int resource, ArrayList<Address> addresses){
        super(context, resource, addresses);

        this.addresses = addresses;
        this.mcontext = context;
        //this.imgId = ImageId;
        Resource = resource;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public  class Holder
    {
        TextView usermobile,useraddresslocation,useraddres1,useraddres2,useraddres3,usercity,userstate,userpin;
        ImageView optionMenu;
        RadioButton mRadioButton;

    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final AddressAdapter.Holder holder=new AddressAdapter.Holder();
        View rowView;
        final Address address = addresses.get(position);
        rowView = inflater.inflate(R.layout.address_item_layout, null);
        holder.usermobile=(TextView) rowView.findViewById(R.id.userMobile);
        holder.useraddresslocation = (TextView) rowView.findViewById(R.id.userLocationName);
        holder.useraddres1=(TextView) rowView.findViewById(R.id.userAddreddLine1);
        holder.useraddres2=(TextView) rowView.findViewById(R.id.userAddreddLine2);
        holder.useraddres3=(TextView) rowView.findViewById(R.id.userAddreddLine3);
        holder.usercity=(TextView) rowView.findViewById(R.id.userCity);
        holder.userstate=(TextView) rowView.findViewById(R.id.userState);
        holder.userpin = (TextView) rowView.findViewById(R.id.userpin);
        holder.optionMenu = (ImageView) rowView.findViewById(R.id.userOptionMenu);
        holder.mRadioButton = (RadioButton) rowView.findViewById(R.id.selectedadd);

        holder.usermobile.setText(address.getUserMobile());
        holder.useraddresslocation.setText(address.getUserLocationName());
        holder.useraddres1.setText(address.getUserAddreddLine1());
        holder.useraddres2.setText(address.getUserAddressLine2());
        holder.useraddres3.setText(address.getUserAddressLine3());
        holder.usercity.setText(address.getUserCity());
        holder.userstate.setText(address.getUserState());
        holder.optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mcontext, holder.optionMenu);
                popupMenu.inflate(R.menu.address_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.editAddress:
                                //handle menu1 click
                                break;
                            case R.id.deleteAddress:
                                //handle menu2 click
                                break;
                            //case R.id.menu3:
                                //handle menu3 click
                              //  break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        holder.mRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position != mSelectedPosition && mSelectedRB != null){
                    mSelectedRB.setChecked(false);
                }
                MyAddressActivity.chooseaddr=1;
                mSelectedPosition = position;
                MyAddressActivity.addressid = (address.getUserAddressID().toString());
                Log.i("JSON", MyAddressActivity.addressid);

                mSelectedRB = (RadioButton) view;
            }
        });


        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(v.getContext(), CategoryActivity.class).putExtra("categoryId",address.getStatus());
                //v.getContext().startActivity(intent);
                // TODO Auto-generated method stub
            }
        });

        return rowView;
    }
}

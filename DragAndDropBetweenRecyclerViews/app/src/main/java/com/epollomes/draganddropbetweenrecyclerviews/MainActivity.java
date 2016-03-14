package com.epollomes.draganddropbetweenrecyclerviews;

import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public class Item {
        Drawable ItemDrawable;
        String ItemString;

        Item(Drawable drawable, String t) {
            ItemDrawable = drawable;
            ItemString = t;
        }
    }


    class PassObject {
        View view;
        Item item;
        List<Item> srcList;

        PassObject(View v, Item i, List<Item> s) {
            view = v;
            item = i;
            srcList = s;
        }
    }

    public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ViewHolder> {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            context = c;
            list = l;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row, parent, false);
            return new ViewHolder(rowView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder2, final int position) {

            final ViewHolder holder = holder2;

            holder.mItem = list.get(position);
            holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Item selectedItem = (Item) (v.getParent().getParent().getItemAtPosition(position));
                    // ItemsListAdapter associatedAdapter = (ItemsListAdapter) (holder.getAdapter());
                    // List<Item> associatedList = associatedAdapter.getList();
                    PassObject passObj = new PassObject(holder.mView, holder.mItem, list);
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(holder.mView);
                    holder.mView.startDrag(data, shadowBuilder, passObj, 0);
                    return true;
                }
            });

            holder.mView.setOnDragListener(new ItemOnDragListener(list.get(position)));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public List<Item> getList() {
            return list;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView icon;
            public final TextView text;
            public Item mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                icon = (ImageView) view.findViewById(R.id.rowImageView);
                text = (TextView) view.findViewById(R.id.rowTextView);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + text.getText() + "'";
            }
        }
    }

    List<Item> items1, items2;
    RecyclerView recyclerView1, recyclerView2;
    ItemsListAdapter myItemsListAdapter1, myItemsListAdapter2;
    LinearLayoutListView area1, area2;
    TextView prompt;
    //Used to resume original color in drop ended/exited
    int resumeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView1 = (RecyclerView) findViewById(R.id.listview1);
        recyclerView2 = (RecyclerView) findViewById(R.id.listview2);

        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        area1 = (LinearLayoutListView) findViewById(R.id.pane1);
        area2 = (LinearLayoutListView) findViewById(R.id.pane2);

        area1.setOnDragListener(myOnDragListener);
        area2.setOnDragListener(myOnDragListener);

        area1.setRecyclerView(recyclerView1);
        area2.setRecyclerView(recyclerView2);

        initItems();
        myItemsListAdapter1 = new ItemsListAdapter(this, items1);
        myItemsListAdapter2 = new ItemsListAdapter(this, items2);

        recyclerView1.setAdapter(myItemsListAdapter1);
        recyclerView2.setAdapter(myItemsListAdapter2);

		/*
        //Auto scroll to end of ListView
		recyclerView1.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		recyclerView2.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listView3.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		*/

        // recyclerView1.setOnItemClickListener(listOnItemClickListener);
        // recyclerView2.setOnItemClickListener(listOnItemClickListener);

        // recyclerView1.setOnItemLongClickListener(myOnItemLongClickListener);
        // recyclerView2.setOnItemLongClickListener(myOnItemLongClickListener);

        prompt = (TextView) findViewById(R.id.prompt);
        // make TextView scrollable
        prompt.setMovementMethod(new ScrollingMovementMethod());
        //clear prompt area if LongClick
        prompt.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                prompt.setText("");
                return true;
            }
        });

        resumeColor = getResources().getColor(android.R.color.background_light);
    }


    AdapterView.OnItemLongClickListener myOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            Item selectedItem = (Item) (parent.getItemAtPosition(position));

            ItemsListAdapter associatedAdapter = (ItemsListAdapter) (parent.getAdapter());
            List<Item> associatedList = associatedAdapter.getList();

            PassObject passObj = new PassObject(view, selectedItem, associatedList);

            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, passObj, 0);

            return true;
        }

    };


    View.OnDragListener myOnDragListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            String area;
            if (v == area1) {
                area = "area1";
            } else if (v == area2) {
                area = "area2";
            } else {
                area = "unknown";
            }

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    prompt.append("ACTION_DRAG_STARTED: " + area + "\n");
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    prompt.append("ACTION_DRAG_ENTERED: " + area + "\n");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    prompt.append("ACTION_DRAG_EXITED: " + area + "\n");
                    break;
                case DragEvent.ACTION_DROP:
                    prompt.append("ACTION_DROP: " + area + "\n");

                    PassObject passObj = (PassObject) event.getLocalState();
                    View view = passObj.view;
                    Item passedItem = passObj.item;
                    List<Item> srcList = passObj.srcList;
                    RecyclerView oldParent = (RecyclerView) view.getParent();
                    ItemsListAdapter srcAdapter = (ItemsListAdapter) (oldParent.getAdapter());

                    LinearLayoutListView newParent = (LinearLayoutListView) v;
                    ItemsListAdapter destAdapter = (ItemsListAdapter) (newParent.recyclerView.getAdapter());
                    List<Item> destList = destAdapter.getList();

                    if (removeItemToList(srcList, passedItem)) {
                        addItemToList(destList, passedItem);
                    }

                    srcAdapter.notifyDataSetChanged();
                    destAdapter.notifyDataSetChanged();

                    //smooth scroll to bottom
                    newParent.recyclerView.smoothScrollToPosition(destAdapter.getItemCount() - 1);

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    prompt.append("ACTION_DRAG_ENDED: " + area + "\n");
                default:
                    break;
            }

            return true;
        }

    };


    class ItemOnDragListener implements View.OnDragListener {

        Item me;

        ItemOnDragListener(Item i) {
            me = i;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    prompt.append("Item ACTION_DRAG_STARTED: " + "\n");
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    prompt.append("Item ACTION_DRAG_ENTERED: " + "\n");
                    v.setBackgroundColor(0x30000000);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    prompt.append("Item ACTION_DRAG_EXITED: " + "\n");
                    v.setBackgroundColor(resumeColor);
                    break;
                case DragEvent.ACTION_DROP:
                    prompt.append("Item ACTION_DROP: " + "\n");

                    PassObject passObj = (PassObject) event.getLocalState();
                    View view = passObj.view;
                    Item passedItem = passObj.item;
                    List<Item> srcList = passObj.srcList;
                    RecyclerView oldParent = (RecyclerView) view.getParent();
                    ItemsListAdapter srcAdapter = (ItemsListAdapter) (oldParent.getAdapter());

                    RecyclerView newParent = (RecyclerView) v.getParent();
                    ItemsListAdapter destAdapter = (ItemsListAdapter) (newParent.getAdapter());
                    List<Item> destList = destAdapter.getList();

                    int removeLocation = srcList.indexOf(passedItem);
                    int insertLocation = destList.indexOf(me);
                /*
                 * If drag and drop on the same list, same position,
				 * ignore
				 */
                    if (srcList != destList || removeLocation != insertLocation) {
                        if (removeItemToList(srcList, passedItem)) {
                            destList.add(insertLocation, passedItem);
                        }

                        srcAdapter.notifyDataSetChanged();
                        destAdapter.notifyDataSetChanged();
                    }

                    v.setBackgroundColor(resumeColor);

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    prompt.append("Item ACTION_DRAG_ENDED: " + "\n");
                    v.setBackgroundColor(resumeColor);
                default:
                    break;
            }

            return true;
        }

    }


    private void initItems() {
        items1 = new ArrayList<Item>();
        items2 = new ArrayList<Item>();

        TypedArray arrayDrawable = getResources().obtainTypedArray(R.array.resicon2);
        TypedArray arrayText = getResources().obtainTypedArray(R.array.restext2);

        for (int i = 0; i < arrayDrawable.length(); i++) {
            Drawable d = arrayDrawable.getDrawable(i);
            String s = arrayText.getString(i);
            Item item = new Item(d, s);
            items1.add(item);
        }

        arrayDrawable.recycle();
        arrayText.recycle();
    }

    private boolean removeItemToList(List<Item> l, Item it) {
        boolean result = l.remove(it);
        return result;
    }

    private boolean addItemToList(List<Item> l, Item it) {
        boolean result = l.add(it);
        return result;
    }
}

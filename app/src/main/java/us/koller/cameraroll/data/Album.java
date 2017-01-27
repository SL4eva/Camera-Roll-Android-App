package us.koller.cameraroll.data;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;

import us.koller.cameraroll.util.SortUtil;

public class Album implements Parcelable, SortUtil.Sortable {

    private ArrayList<AlbumItem> albumItems;
    private String path;

    public boolean hiddenAlbum = false;

    public Album() {
        albumItems = new ArrayList<>();
    }

    public Album setPath(String path) {
        this.path = path;
        return this;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return new File(path).getName();
    }

    @Override
    public long getDate(Activity context) {
        long newestItem = -1;
        for (int i = 0; i < albumItems.size(); i++) {
            if (albumItems.get(i).getDate(context) > newestItem) {
                newestItem = albumItems.get(i).getDate(context);
            }
        }
        return newestItem;
    }

    public ArrayList<AlbumItem> getAlbumItems() {
        return albumItems;
    }

    private Album(Parcel parcel) {
        path = parcel.readString();
        hiddenAlbum = Boolean.parseBoolean(parcel.readString());
        albumItems = new ArrayList<>();
        albumItems = parcel.createTypedArrayList(AlbumItem.CREATOR);
    }

    @Override
    public String toString() {
        return getName() + ": " + getAlbumItems().toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(path);
        parcel.writeString(String.valueOf(hiddenAlbum));
        AlbumItem[] albumItems = new AlbumItem[this.albumItems.size()];
        for (int k = 0; k < albumItems.length; k++) {
            albumItems[k] = this.albumItems.get(k);
        }
        parcel.writeTypedArray(albumItems, 0);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Album createFromParcel(Parcel parcel) {
            return new Album(parcel);
        }

        @Override
        public Album[] newArray(int i) {
            return new Album[i];
        }
    };
}
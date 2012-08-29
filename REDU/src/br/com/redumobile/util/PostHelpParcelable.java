package br.com.redumobile.util;

import android.os.Parcel;
import android.os.Parcelable;
import br.com.redumobile.entity.util.PostHelper;

public class PostHelpParcelable implements Parcelable {

	private PostHelper postagemSelecionada;
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(postagemSelecionada);
	}
	
    public static final Parcelable.Creator<PostHelpParcelable> CREATOR = new Parcelable.Creator<PostHelpParcelable>() {
        public PostHelpParcelable createFromParcel(Parcel in) {
            return new PostHelpParcelable(in);
        }

        public PostHelpParcelable[] newArray(int size) {
            return new PostHelpParcelable[size];
        }
    };

    private PostHelpParcelable(Parcel in) {
        postagemSelecionada = (PostHelper) in.readValue(PostHelper.class.getClassLoader());
    }

}

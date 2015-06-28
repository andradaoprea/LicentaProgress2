package galerie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrada on 19.06.2015.
 */
public class APJsonParser {

    public static ArrayList<Picture> parseFeed(String content){

        try {
            JSONArray ar = new JSONArray(content);
            ArrayList<Picture> tablouList = new ArrayList<>();

            for(int i=0; i<ar.length();i++){

                JSONObject obj = ar.getJSONObject(i);
                Picture pic = new Picture();

                pic.setPictureId(obj.getInt("idTablou"));
                pic.setNamePic(obj.getString("nume"));
                pic.setDescriptionPic(obj.getString("descriere"));
                pic.setNumeAutor(obj.getString("numeAutor"));
                pic.setDescriereAutor(obj.getString("descriereAutor"));
                pic.setPhoto(obj.getString("pozaTablou"));
                tablouList.add(pic);
            }

            return tablouList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}

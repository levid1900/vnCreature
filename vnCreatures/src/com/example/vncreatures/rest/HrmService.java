package com.example.vncreatures.rest;

import java.lang.ref.WeakReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.vncreatures.common.ServerConfig;
import com.example.vncreatures.common.Utils;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureModel;

public class HrmService {
	private Callback mCallback = null;

	public interface Callback {
		public void onGetAllCreaturesDone(final CreatureModel creatureModel);

		public void onError();
	}

	public void setCallback(final Callback callback) {
		mCallback = callback;
	}

	public boolean requestAllCreature(String page) {
		GetAllCreatureTask task = new GetAllCreatureTask();
		task.execute(page);

		return true;
	}

	public boolean requestCreaturesByName(String name, String page) {
		GetCreaturesByNameTask task = new GetCreaturesByNameTask();
		task.execute(page, name);

		return true;
	}

	public boolean requestCreaturesById(String id) {
		GetCreaturesByIdTask task = new GetCreaturesByIdTask();
		task.execute(id);

		return true;
	}
	
	public void downloadImages(String imgId, String loai, ImageView imageView) {
        BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
        task.execute(String.format(ServerConfig.IMAGE_PATH, imgId));
    }


	protected String getAllCreatures(String page) {
		String result = "";
		String request = String.format(ServerConfig.GET_ALL_CREATURE);
		RestClient client = new RestClient(request);
		client.addParam("getAllNameCreature", "");
		client.addParam("format", "json");
		client.addParam("page", page);
		client.addParam("recordPerPage", ServerConfig.NUM_PER_PAGE);

		try {
			client.execute(RestClient.RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	protected String getCreaturesByName(String page, String name) {
		String result = "";
		String request = String.format(ServerConfig.GET_ALL_CREATURE_BY_NAME);
		RestClient client = new RestClient(request);
		client.addParam("getCreatureByName", "");
		client.addParam("format", "json");
		client.addParam("creatureName", name);
		client.addParam("page", page);
		client.addParam("recordPerPage", ServerConfig.NUM_PER_PAGE);

		try {
			client.execute(RestClient.RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	protected String getCreaturesById(String id) {
		String result = "";
		String request = String.format(ServerConfig.GET_CREATURE_BY_ID);
		RestClient client = new RestClient(request);
		client.addParam("getCreatureById", "");
		client.addParam("format", "json");
		client.addParam("id", id);

		try {
			client.execute(RestClient.RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public CreatureModel parseCreatureModelFromJSON(String data) {
		if (data == null || data == "")
			return null;

		CreatureModel creatureModel = new CreatureModel();

		if (data.indexOf('{') > -1)
			data = data.substring(data.indexOf('{'));
		if (data.lastIndexOf('}') > -1)
			data = data.substring(0, data.lastIndexOf('}') + 1);

		try {
			JSONObject creaturesObj = new JSONObject(data);
			if (!creaturesObj.has("creatures"))
				return null;

			JSONArray creatureListObj = creaturesObj.getJSONArray("creatures");
			for (int i = 0; i < creatureListObj.length(); i++) {
				Creature creature = new Creature();
				JSONObject creatureObj = creatureListObj.getJSONObject(i);
				String stringVal = "", key = "";
				int intVal = -1;
				key = "creature";
				if (!creatureObj.has(key))
					continue;
				creatureObj = creatureObj.getJSONObject(key);

				key = "ID";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setId(stringVal);
				key = "Viet";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setvName(stringVal);
				key = "Latin";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setLatin(stringVal);
				key = "Img";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setImageId(stringVal);
				key = "Loai";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setLoaiName(stringVal);
				key = "Description";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setDescription(stringVal);
				key = "AuthorName";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setAuthor(stringVal);

				// Add to the model
				creatureModel.add(creature);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return creatureModel;
	}

	private class GetAllCreatureTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String page = params[0];
			String result = getAllCreatures(page);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mCallback != null) {
				if (result == null) {
					mCallback.onError();
				} else {
					CreatureModel creatureModel = parseCreatureModelFromJSON(result);
					mCallback.onGetAllCreaturesDone(creatureModel);
				}
			}
		}
	}

	private class GetCreaturesByNameTask extends
			AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String page = params[0];
			String name = params[1];
			String result = getCreaturesByName(page, name);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mCallback != null) {
				if (result == null) {
					mCallback.onError();
				} else {
					CreatureModel creatureModel = parseCreatureModelFromJSON(result);
					mCallback.onGetAllCreaturesDone(creatureModel);
				}
			}
		}
	}

	private class GetCreaturesByIdTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String creatureId = params[0];
			String result = getCreaturesById(creatureId);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mCallback != null) {
				if (result == null) {
					mCallback.onError();
				} else {
					CreatureModel creatureModel = parseCreatureModelFromJSON(result);
					mCallback.onGetAllCreaturesDone(creatureModel);
				}
			}
		}
	}
	
	private class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	    private String url;
	    private final WeakReference<ImageView> imageViewReference;

	    public BitmapDownloaderTask(ImageView imageView) {
	        imageViewReference = new WeakReference<ImageView>(imageView);
	    }

	    @Override
	    // Actual download method, run in the task thread
	    protected Bitmap doInBackground(String... params) {
	         // params comes from the execute() call: params[0] is the url.
	         return Utils.downloadBitmap(params[0]);
	    }

	    @Override
	    // Once the image is downloaded, associates it to the imageView
	    protected void onPostExecute(Bitmap bitmap) {
	        if (isCancelled()) {
	            bitmap = null;
	        }

	        if (imageViewReference != null) {
	            ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
	}
}

package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MainControl implements Initializable {

	@FXML
	private Button playPauseButton;
	@FXML
	private Button nextButton;
	@FXML
	private Label musicText;
	@FXML
	private ListView<String> musicList;
	@FXML
	private TextField textSet;
	MediaPlayer mp = null;
	Media me;
	private boolean onOff = false;

	private ArrayList<File> playList;
	private ArrayList<String> nameList;
	private int playNum = -1;
	private boolean firstcheck = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		playPauseButton.setOnAction(e -> {
			if (!onOff) {
				playPauseButton.setText("play");
				pause();
			} else {
				playPauseButton.setText("pause");
				play();
			}
			onOff = !onOff;
		});

		nextButton.setOnAction(e ->

		{
			pause();
			setStartMusic();
		});

		textSet.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER)
				{
					setMusic(textSet.getText());
				}
			}
		});
		

	}

	private void setMusic(String data) {
		mp3Filter(data, fileFilter("C:\\Users\\10310_LeeBC\\Music\\음악"));

		if (firstcheck) {
			pause();
			playNum = -1;
		}
		if (playList.size() != 0) {
			setStartMusic();
			firstcheck = true;
			ObservableList<String> musicListData = FXCollections.observableArrayList(nameList);
			musicList.setItems(musicListData);
		}

	}

	private void setStartMusic() {
		File startFile = mp3Output();
		musicText.setText(startFile.getName());
		me = new Media(startFile.toURI().toString());
		mp = new MediaPlayer(me);
		mp.setAutoPlay(true);
		mp.setOnEndOfMedia(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setStartMusic();
			}
			
		});
		
		mp.onEndOfMediaProperty();
	}

	private Vector<String> fileFilter(String address) {
		String path = address;
		File dirFile = new File(path);
		File[] fileList = dirFile.listFiles();
		Vector<String> v = new Vector<String>();
		for (File tempFile : fileList) {
			if (tempFile.isFile()) {
				if ((tempFile + "").contains("mp3")) {
					v.add(tempFile + "");
				}
			}
		}

		return v;
	}

	private void mp3Filter(String musicName, Vector<String> v) {
		playList = new ArrayList<File>();
		for (String musicFile : v) {
			if (musicFile.contains(musicName)) {
				playList.add(new File(musicFile));
			}
		}

		nameList = new ArrayList<String>();
		for (File tempFile : playList) {
			String tempName = "";
			for (int i = 0; i < tempFile.getName().indexOf(".mp3"); i++) {
				tempName += tempFile.getName().charAt(i);
			}
			nameList.add(tempName);
		}

	}

	private File mp3Output() {
		if (playNum >= playList.size() - 1)
			playNum = -1;
		playNum++;
		System.out.println(playNum);
		return playList.get(playNum);
	}

	// 시작
	public void play() {
		mp.play();
		mp.setRate(1);
	}

	// 정지
	public void pause() {
		mp.pause();
	}

	public void fast(ActionEvent event) {
		// mp.setRate(1);// 노멀 스피드
		mp.setRate(2);// 2배 스피드
		// mp.setRate(0.5);// 0.5배 스피드

	}

	public void slow(ActionEvent event) {
		mp.setRate(.5);
	}

	public void reload(ActionEvent event) {
		mp.seek(mp.getStartTime());
		mp.play();
	}

	public void start(ActionEvent event) {
		mp.seek(mp.getStartTime());
		mp.stop();
	}

	public void last(ActionEvent event) {
		mp.seek(mp.getTotalDuration());
		mp.stop();
	}
}
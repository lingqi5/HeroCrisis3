package Game.Start;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Bgm {
    void playMusic(String musicLocation) {
        try {
            File musicPath = new File(musicLocation);
            if (musicPath.exists()) {
                //音乐流
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                //静态
                Clip clip = AudioSystem.getClip();
                //输入clip
                clip.open(audioInput);
                //启动音乐
                clip.start();
                //循环播放
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

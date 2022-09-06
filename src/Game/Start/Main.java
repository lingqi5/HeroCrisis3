package Game.Start;


public class Main {
    public static void main(String[] args) {
        new StartFrame();
        String filepath = "src\\MUSIC\\bgm.wav";
        Bgm musicObject = new Bgm();
        musicObject.playMusic(filepath);
    }
}

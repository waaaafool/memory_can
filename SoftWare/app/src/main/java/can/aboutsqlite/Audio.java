package can.aboutsqlite;

public class Audio {
    private int audio_id;
    private  String audio_filename;
    private int memo_id;
    public Audio(String path,int memo_id){
        this.audio_filename=audio_filename;
        this.memo_id=memo_id;
    }
    public int getMemo_id() {
        return memo_id;
    }

    public void setMemo_id(int memo_id) {
        this.memo_id = memo_id;
    }
    public String getAudio_filename() {
        return audio_filename;
    }
    public void setAudio_filename(String audio_filename) {
        this.audio_filename = audio_filename;
    }
    public int getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(int audio_id) {
        this.audio_id = audio_id;
    }

}

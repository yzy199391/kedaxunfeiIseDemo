package yzy.kedaxunfei.research.demo;

import org.apache.commons.codec.binary.Base64;
import yzy.kedaxunfei.research.demo.Util.ConvertorUtil;
import yzy.kedaxunfei.research.demo.Util.FileUtil;
import yzy.kedaxunfei.research.demo.Util.HttpUtil;
import yzy.kedaxunfei.research.demo.Util.SignUtil;

import java.util.TreeMap;

/**
 * @author yanzy
 * @date 2019/2/19 上午10:39
 * @description 腾讯云-智聆语音评测
 */
public class Tencent {
    // 音频文件地址
    private static final String AUDIO_PATH = "/Users/xmly/Desktop/testAudio.wav";
    private static final String OUT_PATH = "/Users/xmly/Desktop/testAudio.pcm";

    private static final String INIT_URL = "https://soe.tencentcloudapi.com/?";
    private static final String INIT_ACTION = "InitOralProcess";
    private static final String INIT_VERSION = "2018-07-24";
    private static final String INIT_SESSION_ID = "test_session_01";
    private static final String INIT_REF_TEXT = "This is an example of sentence test";
    private static final Integer INIT_WORK_MODE = 1; //0-流式；1-非流式一次性评估
    private static final Integer INIT_EVAL_MODE = 1; //0-词模式、1-句子模式、2-段落模式、3-自由说模式
    //评价苛刻指数 1.0-4.0逐渐增大
    private static final Float INIT_SCORE_COEFF = 2.1f;

    private static final String TRANSMISSION_URL = "https://soe.tencentcloudapi.com/?";
    private static final String TRANSMISSION_ACTION = "TransmitOralProcess";

    //初始化发音评估过程
    private String initProcess() throws Exception {
        String canonicalQueryString = "SessionId=" + INIT_SESSION_ID
                + "&WorkMode=" + INIT_WORK_MODE
                + "&RefText=" + INIT_REF_TEXT
                + "&EvalMode=" + INIT_EVAL_MODE
                + "&ScoreCoeff=" + INIT_SCORE_COEFF;

        String url = INIT_URL + canonicalQueryString;

        TreeMap<String, String> headMap = SignUtil.sign(INIT_ACTION, INIT_VERSION, canonicalQueryString);
        return HttpUtil.doGet(url, headMap);
    }

    //发音数据传输接口
    private String audioTransmission(int fileType, int seqId, int voiceEncodeType, String userVoiceData) throws Exception {
        String canonicalQueryString = "Action=" + TRANSMISSION_ACTION
                + "&SessionId=" + INIT_SESSION_ID
                + "&VoiceFileType=" + fileType
                + "&SeqId=" + seqId
                + "&VoiceEncodeType=" + voiceEncodeType
                + "&IsEnd=" + 0
                + "&UserVoiceData=" +  userVoiceData;
        String url = TRANSMISSION_URL + canonicalQueryString;
        TreeMap<String, String> headMap = SignUtil.sign(TRANSMISSION_ACTION, INIT_VERSION, canonicalQueryString);
        return HttpUtil.doGet(url, headMap);
    }

    public static void main(String[] args) throws Exception {
        Tencent tencent = new Tencent();
        System.out.println(tencent.initProcess());
        String pcmFilePath = ConvertorUtil.convertAudioFiles(AUDIO_PATH, OUT_PATH);
        byte[] audioByteArray = FileUtil.read(pcmFilePath);
        String audioBase64 = new String(Base64.encodeBase64(audioByteArray), "UTF-8");
        System.out.println("audio string: " + audioBase64);
        System.out.println("audio transmit result: " + tencent.audioTransmission(1, 0, 1, audioBase64));
    }
}

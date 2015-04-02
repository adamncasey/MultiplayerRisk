package networking.networkplayer;

import networking.message.Message;

/**
 * Created by Adam on 02/04/2015.
 */
public class MoveProcessResult {
    public final static MoveProcessResult MORE_WORK_NEEDED = new MoveProcessResult(true, false);
    public final static MoveProcessResult NO_RESPONSE_NEEDED = new MoveProcessResult(false, false);

    public final Message message;
    public final boolean moreWorkNeeded;
    public final boolean responseNeeded;

    public MoveProcessResult(Message message) {
        this(message, false, true);
    }

    private MoveProcessResult(Message message, boolean moreWorkNeeded, boolean responseNeeded) {
        this.message = message;
        this.moreWorkNeeded = moreWorkNeeded;
        this.responseNeeded = responseNeeded;
    }

    private MoveProcessResult(boolean moreWorkNeeded, boolean responseNeeded) {
        this(null, moreWorkNeeded, responseNeeded);
    }
}

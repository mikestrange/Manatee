package client.body.room;

import net.utils.FtpRead;
import interfaces.ISocketResult;

public class SkillObj implements ISocketResult {

	//jump=1;
	public int uid;
	public int skill_type;
	public int skill_id;
	
	public void resolve(FtpRead bytes) {
		uid = bytes.readInt();
		skill_type = bytes.readUShort();
		skill_id = bytes.readInt();
	}
	//ends
}


public class Token {

	String token;
	int type;
	
	public Token(String token, int type) {
		this.token = token;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return token + " == " + type;
	}
}
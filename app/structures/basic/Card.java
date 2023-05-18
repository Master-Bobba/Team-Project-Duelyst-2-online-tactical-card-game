package structures.basic;


/**
 * This is the base representation of a Card which is rendered in the player's hand.
 * A card has an id, a name (cardname) and a manacost. A card then has a large and mini
 * version. The mini version is what is rendered at the bottom of the screen. The big
 * version is what is rendered when the player clicks on a card in their hand.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Card implements Playable{
	
	int id;
	public static int type;
	public int positionInHand;
	private boolean moveModifier;
	
	String cardname;
	int manacost;
	
	MiniCard miniCard;
	BigCard bigCard;
	
	public Card() {};
	
	public Card(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard) {
		super();
		this.id = id;
		this.cardname = cardname;
		this.manacost = manacost;
		this.miniCard = miniCard;
		this.bigCard = bigCard;

		System.out.println(cardname);
		type = 2;
	}
	public void setModifier(){
		moveModifier = false;
		switch(this.cardname){
			case "Ironcliff Guardian":
				moveModifier = true;
				return;
			case "Planar Scout":
				moveModifier = true;
				return;
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCardname() {
		return cardname;
	}
	public void setCardname(String cardname) {
		this.cardname = cardname;
	}
	public int getManacost() {
		return manacost;
	}
	public void setManacost(int manacost) {
		this.manacost = manacost;
	}
	public MiniCard getMiniCard() {
		return miniCard;
	}
	public void setMiniCard(MiniCard miniCard) {
		this.miniCard = miniCard;
	}
	public BigCard getBigCard() {
		return bigCard;
	}
	public void setBigCard(BigCard bigCard) {
		this.bigCard = bigCard;
	}
	public void setPositionInHand(int pos){
		positionInHand = pos;
	}
	public int getPositionInHand(){
		return positionInHand;
	}
	public boolean getMoveModifier(){
		return moveModifier;
	}

	
}

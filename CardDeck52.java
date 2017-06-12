package gameBasics;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import gameBasics.CardDeck52.Card.Sign;
import static gameBasics.CardDeck52.Card.Sign.*;

/**
 * Created by Amir on 25.05.2017.
 */

public class CardDeck52 {
	public static final int CARDS_COUNT            		= 52;

    public static class Card implements Comparable<Card> {
    	public static enum Sign {
            Spades, Clubs, Diamonds, Hearts
        }
    	
    	public static final int MIN_CARD_VALUE			= 2;
        public static final int MAX_CARD_VALUE          = 14;
        
        public static final int TWO						= MIN_CARD_VALUE;
        public static final int THREE					= 3;
        public static final int FOUR 					= 4;
        public static final int FIVE					= 5;
        public static final int SIX						= 6;
        public static final int SEVEN					= 7;
        public static final int EIGTH					= 8;
        public static final int NINE					= 9;
        public static final int TEN						= 10;
        public static final int JACK					= 11;
        public static final int QUEEN					= 12;
        public static final int KING					= 13;
        public static final int ACE						= MAX_CARD_VALUE;
        
        private static final int Face_CARD_STARTVALUE 	= JACK;
        
        private final int value;
        private final Sign sign;

        public Card(int value, Sign sign) {
            this.value = value;
            this.sign = sign;
        }

        public final int value() {
            return this.value;
        }

        public final Sign sign() {
            return this.sign;
        }
        
        public boolean equals(Card that) {
        	return this.compareTo(that) == 0;
        }

        @Override
        public int compareTo(Card that) {
            return that == null ? 1 : this.value() - that.value();
        }
        
        @Override
        public String toString() {
            int value = this.value();
            String signname = this.sign().name();

            if(value >= Face_CARD_STARTVALUE)
                return value == JACK ?  "Jack("+signname+")" :
                       value == QUEEN ? "Queen("+signname+")" :
                       value == KING ?  "King("+signname+")" :
                       "Ace("+signname+")";

            return value+"("+signname+")";
        }
        
        public static boolean inLineAsc(Card that1, Card that2) {
        	return that1.compareTo(that2) == -1;// || isAce(this) && isValue(that, TWO);
        }
        
        public static boolean inLineDesc(Card that1, Card that2) {
        	return that1.compareTo(that2) == 1;// || isAce(that) && isValue(this, TWO);
        }
        
        public static boolean isValue(Card that, int value) {
        	return that.value() == value;
        }
        
        public static boolean isAce(Card that) {
        	return isValue(that, ACE);
        }
        
        public static boolean isKing(Card that) {
        	return isValue(that, KING);
        }
        
        public static boolean isQueen(Card that) {
        	return isValue(that, QUEEN);
        }
        
        public static boolean isJack(Card that) {
        	return isValue(that, JACK);
        }
        
        public static boolean isSign(Card that, Card that2) {
        	return that.sign().equals(that2.sign());
        }
        
        public static boolean isSign(Card that, Sign thatSign) {
        	return that.sign().equals(thatSign);
        }
        
        public static boolean isDiamond(Card that) {
        	return isSign(that, Diamonds);
        }
        
        public static boolean isClubs(Card that) {
        	return isSign(that, Clubs);
        }
        
        public static boolean isSpades(Card that) {
        	return isSign(that, Spades);
        }
        
        public static boolean isHearts(Card that) {
        	return isSign(that, Hearts);
        }
    }

    private List<Card> cardDeck;
    private int deckIndex;

    public CardDeck52() {
        this.cardDeck = new ArrayList<>(CARDS_COUNT);
        
        for(int cardvalue = Card.MIN_CARD_VALUE; cardvalue <= Card.MAX_CARD_VALUE; cardvalue++)
        	for(Sign sign : Sign.values())
        		this.cardDeck.add(new Card(cardvalue, sign));

        this.reset();
    }
    
    private void _shuffle() {
        Collections.shuffle(this.cardDeck);
        this.cut();
    }
    
    private Card _deal() {
        Card card = this.cardDeck.get(deckIndex);
        this._onDealed();
        return card;
    }

    private boolean _canDeal() {
        return this.deckIndex >= 0;
    }

    private void _onDealed() {
        this.deckIndex--;
    }

    protected void cut(int cutIndex) {
    	List<Card> firstHalf  = new ArrayList<>(this.cardDeck.subList(0, cutIndex));
    	List<Card> secondHalf = new ArrayList<>(this.cardDeck.subList(cutIndex, this.deckIndex + 1));
        this.cardDeck.clear();
        this.cardDeck.addAll(secondHalf);
        this.cardDeck.addAll(firstHalf);
    }
    
    public void cut() {
    	int randomCutIndex = new Random().nextInt(CARDS_COUNT);//) - CARDS_COUNT / 4) + CARDS_COUNT / 5;
    	this.cut(randomCutIndex);
    }

    public void reset() {
        this.deckIndex = CARDS_COUNT - 1;
        this._shuffle();
    }

    public void burn() {
    	this.deal();
    }
    
    public void burn(int num) {
    	this.deal(num);
    }
    
    public Card deal() {
        if (!this._canDeal())
            throw new Error("'CardDeck52->deal()' on Empty Deck! deckIndex=" + deckIndex);

        return this._deal();
    }
    
    public List<Card> deal(int num) {
    	List<Card> cards = new ArrayList<>(num);
    	for(; num > 0; num--)
    		cards.add(this.deal());
    	
    	return cards;
    }
}

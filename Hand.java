package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import gameBasics.CardDeck52.Card;
import gameBasics.CardDeck52.Card.Sign;
import static poker.Hand.CombinationType.*;

public class Hand{
	public static enum CombinationType {
		HighCard,
		OnePair,
		TwoPair,
		ThreeOfAKind,
		Straight,
		Flush,
		FullHouse,
		FourOfAKind,
		StraightFlush,
		RoyalFlush
	}

	public static final class Combination implements Comparable<Combination> {	
		private CombinationType comboType;
		private List<Card> tableCards;
		private List<Card> all5ComboCards;
		private List<Card> all7Cards;
		private Hand hand;
		
		private Combination(List<Card> tableCards, Hand hand) {
			this.tableCards = tableCards;
			this.hand = hand;
			this.all7Cards = new ArrayList<>(7);
			this.all7Cards.addAll(tableCards);
			this.all7Cards.addAll(hand.asList());
			Collections.sort(this.all7Cards);
			this.all5ComboCards = new ArrayList<>(5);
			eval(this);
		}
		
		public CombinationType getComboType() {
			return this.comboType;
		}
		
		public List<Card> getComboCards() {
			return this.all5ComboCards;
		}
		
		public List<Card> getHandCards() {
			return this.hand.asList();
		}
		
		public List<Card> getTableCards() {
			return this.tableCards;
		}
		
		public List<Card> getAll7Cards() {
			List<Card> all7cards = new ArrayList<>(7);
			all7cards.addAll(this.getHandCards());
			all7cards.addAll(this.getTableCards());
			return all7cards;
		}
		
		public boolean is(CombinationType combi) {
			return combi.equals(comboType);
		}
		
		public boolean isHighCard() {
			return this.is(HighCard);
		}
		
		public boolean isOnePair() {
			return this.is(OnePair);
		}
		
		public boolean isTwoPair() {
			return this.is(TwoPair);
		}
		
		public boolean isThreeOfAKind() {
			return this.is(ThreeOfAKind);
		}
		
		public boolean isStraight() {
			return this.is(Straight);
		}
		
		public boolean isFlush() {
			return this.is(Flush);
		}
		
		public boolean isFullHouse() {
			return this.is(FullHouse);
		}
		
		public boolean isFourOfAKind() {
			return this.is(FourOfAKind);
		}
		
		public boolean isStraightFlush() {
			return this.is(StraightFlush);
		}
		
		public boolean isRoyalFlush() {
			return this.is(RoyalFlush);
		}
				
		private static void _filterSign(List<Card> all7Cards, Sign ifFlushSign) {
			int sizeAll7 = all7Cards.size();
			List<Card> allCards = new ArrayList<>(7);
			for(int i = 0; i < sizeAll7; i++)
				if(Card.isSign(all7Cards.get(i), ifFlushSign))
					allCards.add(all7Cards.get(i));
			
			all7Cards.clear();
			all7Cards.addAll(allCards);
		}
		
		private static enum Straights {
			NoStraight, AceStartStraight, Straight
		}
		
		private static Straights _hasStraight(List<Card> all7Cards) {
			int size = all7Cards.size();
			if(size < 5)
				return Straights.NoStraight;
				
			boolean hasAceStraightb[] = new boolean[5];
			hasAceStraightb[0] 	  	  = Card.isAce(all7Cards.get(size - 1));
			int acsCards 			  = 0;
			Card card 			      = null;
			Card card2 				  = null;
			
			for(int j = 0; acsCards < 4 && j < size; j++) {
				card  = all7Cards.get(j);
				card2 = all7Cards.get((j + 1) % size);
				
				if(card.equals(card2))
					continue;
				
				if(hasAceStraightb[0]) {
					hasAceStraightb[1] = hasAceStraightb[1] || Card.isValue(card, Card.TWO);
					hasAceStraightb[2] = hasAceStraightb[2] || Card.isValue(card, Card.THREE);
					hasAceStraightb[3] = hasAceStraightb[3] || Card.isValue(card, Card.FOUR);
					hasAceStraightb[4] = hasAceStraightb[4] || Card.isValue(card, Card.FIVE);
				}
						
				if(Card.inLineAsc(card, card2))
					acsCards++;
				else
					acsCards = 0;
			}
						
			if(acsCards >= 4)
				return Straights.Straight;

			if(hasAceStraightb[0] && 
			   hasAceStraightb[1] && 
			   hasAceStraightb[2] && 
			   hasAceStraightb[3] && 
			   hasAceStraightb[4])
				return Straights.AceStartStraight;
			
			return Straights.NoStraight;
		}
		
		private static void setHighCard(Combination combo, List<Card> all7Cards) {
			combo.comboType = HighCard;
			int startIndex = all7Cards.size() - 2;
			combo.all5ComboCards.add(all7Cards.get(startIndex + 1));
			combo.all5ComboCards.add(all7Cards.get(startIndex));
			all7Cards.removeAll(combo.all5ComboCards);
		}
		
		private static void setOnePair(Combination combo, List<Card> all7Cards) {
			combo.comboType = OnePair;
			for(int i = 0; combo.all5ComboCards.isEmpty(); i++)
				if(all7Cards.get(i).equals(all7Cards.get(i + 1))) 				
					combo.all5ComboCards.addAll(all7Cards.subList(i, i + 2));;
				
			all7Cards.removeAll(combo.all5ComboCards);
		}
		
		private static void setTwoPair(Combination combo, List<Card> all7Cards) {
			combo.comboType = TwoPair;	
			for(int i = all7Cards.size() - 1; combo.all5ComboCards.size() < 4; i--)
				if(all7Cards.get(i).equals(all7Cards.get(i - 1)))				
					combo.all5ComboCards.addAll(all7Cards.subList(i - 1, i + 1));
				
			all7Cards.removeAll(combo.all5ComboCards);
		}
		
		private static void setThreeOfAKind(Combination combo, List<Card> all7Cards) {
			combo.comboType = ThreeOfAKind;
			for(int i = 0; combo.all5ComboCards.size() < 3; i++) 
				if(all7Cards.get(i).equals(all7Cards.get(i + 1))) 	
					combo.all5ComboCards.addAll(all7Cards.subList(i, i + 3));					
				
			all7Cards.removeAll(combo.all5ComboCards);
		}

		private static void setStraight(Combination combo, List<Card> all7Cards, boolean hasAceStartStraight) {			
			combo.comboType 		 = Straight;	
			Card card 				 = null;
			Card card2 				 = null;

			for(int startIndex = all7Cards.size() - 1, found = 0, 
					indexEnsureForwards  = 0, 
					indexEnsureBackwards = 1;; startIndex--) {
				card = all7Cards.get(startIndex);				
				card2 = all7Cards.get(startIndex - 1);
				if(card.equals(card2))
					continue;
				
				if(Card.inLineDesc(card, card2)) {
					if(++found >= 3) {
						if(hasAceStartStraight) 
							combo.all5ComboCards.add(all7Cards.get(all7Cards.size() - 1));
						else {
							while(card2.equals(all7Cards.get(startIndex - ++indexEnsureBackwards)));
							combo.all5ComboCards.add(all7Cards.get(startIndex - indexEnsureBackwards));
						}
						
						combo.all5ComboCards.add(card2);
						combo.all5ComboCards.add(card);
						
						while(card.equals(all7Cards.get(startIndex + ++indexEnsureForwards)));		
						card = all7Cards.get(startIndex + indexEnsureForwards);
						combo.all5ComboCards.add(card);
						
						while(card.equals(all7Cards.get(startIndex + ++indexEnsureForwards)));		
						combo.all5ComboCards.add(all7Cards.get(startIndex + indexEnsureForwards));
						return;
					}
				}
				else
					found = 0;
			}
		}
		
		private static void setFlush(Combination combo, List<Card> all7Cards) {
			combo.comboType = Flush;
			int all7size = all7Cards.size();
			combo.all5ComboCards.addAll(all7Cards.subList(all7size - 5, all7size));
		}
		
		private static void setFullHouse(Combination combo, List<Card> all7Cards) {
			combo.comboType = FullHouse;
			for(int j = all7Cards.size() - 1; combo.all5ComboCards.isEmpty(); j--)
				if(all7Cards.get(j).equals(all7Cards.get(j - 1)) && 
				   all7Cards.get(j).equals(all7Cards.get(j - 2))) 
						combo.all5ComboCards.addAll(all7Cards.subList(j - 2, j + 1));
				
			all7Cards.removeAll(combo.all5ComboCards);
			for(int k = all7Cards.size() - 1; combo.all5ComboCards.size() < 5; k--)
				if(all7Cards.get(k).equals(all7Cards.get(k - 1)))
					combo.all5ComboCards.addAll(all7Cards.subList(k - 1, k + 1));
		}
		
		private static void setFourSome(Combination combo, List<Card> all7Cards) {
			combo.comboType = FourOfAKind;			
			for(int j = all7Cards.size() - 1; combo.all5ComboCards.isEmpty(); j--)
				if(all7Cards.get(j).equals(all7Cards.get(j - 3)))
					combo.all5ComboCards.addAll(all7Cards.subList(j - 3, j + 1));
			
			all7Cards.removeAll(combo.all5ComboCards);
		}
		
		private static void setStraightFlush(Combination combo) {
			combo.comboType = StraightFlush;
		}
		
		private static void setRoyalFlush(Combination combo) {
			combo.comboType = RoyalFlush;
		}
		
		private static void eval(Combination combo) {
			List<Card> all7Cards = combo.getAll7Cards();
			boolean hasPair 		 	= false;
			boolean hasPairControler 	= false;
			boolean has2Pair 		 	= false;
			boolean hasTriple 		 	= false;
			Card hasTripleCard		    = null;
			Straights hasStraight 	 	= Straights.NoStraight;
			boolean hasAceStartStraight = false;
			boolean hasFlush 		 	= false;
			int hasFlushCounter			= 0;
			Sign ifFlushSign 	     	= null;
			boolean hasFullHouse	 	= false;
			boolean hasFourSome 	 	= false;
			boolean hasStraightFlush 	= false;
			boolean hasRoyalFlush    	= false;
			int countEach 			 	= 0;
			
			for(Card card : all7Cards) {
				for(Card cardinner : all7Cards) {					
					if(card.equals(cardinner))
						countEach++;
				
					if(Card.isSign(card, cardinner))
						hasFlushCounter++;
				}
				
				if(hasFlushCounter >= 5)
					ifFlushSign = card.sign();
				
				if(countEach == 2)
					if(!hasPair)
						hasPair = true;
					else if(hasPairControler)
						has2Pair = true;
					else
						hasPairControler = true;
				else if(countEach == 3) 
					if(hasTriple && !card.equals(hasTripleCard)) {
						hasPair = true;
						break;
					}
					else {
						hasTripleCard = card;
						hasTriple = true;
					}
				else if(countEach == 4) {
					hasFourSome = true;
					break;
				}
				countEach 		= 0;
				hasFlushCounter = 0;
			}

			hasFlush 	 		= ifFlushSign != null;
			hasFullHouse 		= hasPair && hasTriple;
			
			if(hasFlush)
				_filterSign(all7Cards, ifFlushSign);
			
			if(!hasFullHouse && !hasFourSome)
				hasStraight  	=  _hasStraight(all7Cards);
			
			hasAceStartStraight = hasStraight.equals(Straights.AceStartStraight);
			hasStraightFlush 	= hasFlush && !hasStraight.equals(Straights.NoStraight);
			
			if(hasStraightFlush) {
				setStraight(combo, all7Cards, hasAceStartStraight);
				hasRoyalFlush = Card.isAce(combo.all5ComboCards.get(4));
			}

			int toAddMax = 0;
			
			if(hasRoyalFlush) 
				setRoyalFlush(combo);
			else if(hasStraightFlush)
				setStraightFlush(combo);			
			else if(hasFourSome) {
				toAddMax = 1;
				setFourSome(combo, all7Cards);
			}
			else if(hasFullHouse)
				setFullHouse(combo, all7Cards);			
			else if(hasFlush) 
				setFlush(combo, all7Cards);	
			else if(!hasStraight.equals(Straights.NoStraight))
				setStraight(combo, all7Cards, hasAceStartStraight);		
			else if(hasTriple) {
				toAddMax = 2;
				setThreeOfAKind(combo, all7Cards);
			}
			else if(has2Pair) {
				toAddMax = 1;
				setTwoPair(combo, all7Cards);
			}
			else if(hasPair) {
				toAddMax = 3;
				setOnePair(combo, all7Cards);
			}
			else {
				toAddMax = 3;
				setHighCard(combo, all7Cards);
			}
			
			int restSize = all7Cards.size() - 1;
			for(; toAddMax > 0 && restSize >= 0; toAddMax--, restSize--)
				combo.all5ComboCards.add(all7Cards.get(restSize));
		}
		
		@Override
		public int compareTo(Combination that) {	
			int compared = getComboType().ordinal() - that.getComboType().ordinal();
			
			if(compared != 0)
				return compared;

			int maxCards = 5;
			int[] order  = null;
			
			switch(this.getComboType()) {
				case HighCard:	
					maxCards = all5ComboCards.size();
					for(int k = 0; k < maxCards; k++) {
						compared = all5ComboCards.get(k).compareTo(that.all5ComboCards.get(k));
						if(compared != 0)
							return compared;
					}
					break;
				case OnePair:
					order = new int[] {0, 2, 3, 4};	
					maxCards = all5ComboCards.size();
					for(int k : order) {
						if(k >= maxCards)
							break;
						
						compared = all5ComboCards.get(k).compareTo(that.all5ComboCards.get(k));
						if(compared != 0)
							return compared;
					}
					break;
				case TwoPair:
					order = new int[] {0, 2, 4};
					for(int k : order) {
						compared = all5ComboCards.get(k).compareTo(that.all5ComboCards.get(k));
						if(compared != 0)
							return compared;
					}
					break;
				case ThreeOfAKind:
					order = new int[] {0, 3, 4};				
					for(int k : order) {
						compared = all5ComboCards.get(k).compareTo(that.all5ComboCards.get(k));
						if(compared != 0)
							return compared;
					}
					break;
				case Straight:
					return all5ComboCards.get(4).compareTo(that.all5ComboCards.get(4));	
				case Flush:
					for(int k = 4; k >= 0; k--) {
						compared = all5ComboCards.get(k).compareTo(that.all5ComboCards.get(k));
						if(compared != 0)
							return compared;
					}
					break;
				case FullHouse:
					 order = new int[] {0, 3};				
					for(int k : order) {
						compared = all5ComboCards.get(k).compareTo(that.all5ComboCards.get(k));
						if(compared != 0)
							return compared;
					}
					break;
				case FourOfAKind:
					order = new int[] {0, 4};
					for(int k : order) {
						compared = all5ComboCards.get(k).compareTo(that.all5ComboCards.get(k));
						if(compared != 0)
							return compared;
					}
					break;
				case StraightFlush:
					return all5ComboCards.get(4).compareTo(that.all5ComboCards.get(4));
				case RoyalFlush:
					return 0;
			}
			return 0; // all equals
		}
	}
	
	public static int HAND_TABLE_CARD_COUNT = 7;
	
	private Card card1;
	private Card card2;
	
	public Combination eval(List<Card> table) {
		return new Combination(table, this);
	}
	
	private void ordered() {		
		if(this.card1.compareTo(this.card2) > 0) {
			Card tempSwap = this.card1;
			this.card1 = this.card2;
			this.card2 = tempSwap;
		}
	}

	public Hand(Card card1, Card card2) {
		this.card1 = card1;
		this.card2 = card2;
		this.ordered();
	}
	
	public Hand() {
	}
	
	public void takeDeal(Card card) {
		if(this.card1 == null)
			this.card1 = card;
		else {
			this.card2 = card;
			this.ordered();
		}
	}
	
	public List<Card> asList() {
		return Arrays.asList(this.card1, this.card2);
	}
}

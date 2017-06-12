# PokerEvaluator
	A simple poker hand evaluator. This evaluator works with any hand size (2, 5, 6 and 7 cards).
	It will evaluate the given hand and return the combination cards (up to 5 cards). 

	Files: 
	Hand.java, CardDeck52.java

	Classes: 
	Hand, Combination, CardDeck52, Card
 	------------------------------------------------------------

	// Examples: 
	Hand hand1 = new Hand(
		new Card(5, Diamonds), // 5 of Diamonds (CardDeck52.Card.Sign.Diamonds)
		new Card(14, Diamonds) // Or: new Card(Card.ACE, Diamonds). Ace of Diamonds
	);

	Hand hand2 = new Hand(
		       new Card(5, Spades), // 5 of Spades
		       new Card(6, Hearts) // 6 of Hearts
		      );
	// Table cards. Flop, Turn and River. 5 Cards on the Table			
	List<Card> table = new ArrayList<Card>() {{
					add(
							new Card(7, Spades)
					);
					add(
							new Card(7, Diamonds)
					);
					add(
							new Card(2, Diamonds)
					);
					add(
							new Card(3, Diamonds)
					);
					add(
							new Card(4, Diamonds)
					);
				}};
      
	Combination combo1 = hand1.eval(table); // Evaluate hand1 with that given table cards
	Combination combo2 = hand2.eval(table);

	System.out.println("Hand1: " +  combo1.getComboType() + " - " + combo1.getComboCards());
	System.out.println("Hand2: " + combo2.getComboType() + " - " + combo2.getComboCards());

	int compared = combo1.compareTo(combo2); // Any Combination can be compared to any other Combination
	if(compared < 0)
		System.out.println("hand1 < hand2");
	else if(compared == 0)
		System.out.println("hand1 = hand2");
	else
		System.out.println("hand1 > hand2");
     
     
     // Output is: 
     Hand1: StraightFlush - [Ace(Diamonds), 2(Diamonds), 3(Diamonds), 4(Diamonds), 5(Diamonds)]
     Hand2: Straight - [3(Diamonds), 4(Diamonds), 5(Spades), 6(Hearts), 7(Spades)]
     hand1 > hand2
     // End of Output
     
     The Combination class is comparable, so you can put Combinations into a list and sort it.
     Example: Collections.sort(Arrays.asList(combo1, combo2))
     
     
     CardDeck52:
     The CardDeck52 has 2 main methods to go with:
     --------------------------------------------
     1) deal() and deal(n), with n >= 1. To deal a card or n cards.
     2) reset() to reset the carddeck, this make it like its a new deck of cards.
     
     Example:
     CardDeck52 deck 	= new CardDeck52();
     Card myFirstHandCard = deck.deal();
     Card mySecondHandCard= deck.deal();
     
     List<Card> tableCards = new ArrayList<Card>(deck.deal(5));
     
     
     I hope this evaluator is bug free, if anyone notices a bug, i will be so happy for feedback.
     
     Enjoy :)
     
     

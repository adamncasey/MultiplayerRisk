Overview

There are several different agents, defined in AgentTypes.java
An agent can be instantiated by using AgentFactory.getAgent(AgentTypes.Type);
or directly, i.e. Agent a = new PassiveAgent();

Each agent must provide a name, a description, and implement player.PlayerController (preferably without any user input ...)

To avoid code duplication, and to better organise different tactics. Agents can use Strategies.
A Strategy is like a player controller, but it only implements a subset of the game stages.

Current agents are composites of different strategies, but an agent does not have to use strategies (non deterministic AIs may use a more complicated system)

List of Agents:

  Not in use / For testing purposes:
    PASSIVE : Uses PassiveStrategy, RandomSrategy
    Uses PassiveStrategy to decide when to attack/fortify (never) and trade in cards, uses RandomStrategy to claim territories / place armies.
    RANDOM : Uses PassiveStrategy, RandomSrategy
    Uses PassiveStrategy to trade in cards, RandomStrategy for everything else.

  ANGRY : Uses PassiveStrategy, RandomStrategy, AggressiveStrategy
    Expanded RANDOM
    Uses AggressiveStrategy to decide whether to attack or fortify, where to fortify, how many dice to roll, and how many armies to move forward when attacking/fortifying.

  GREEDY : RandomStrategy, AggressiveStrategy, CardsStrategy
    Expanded ANGRY
    Uses CardsStrategy to decide when to trade in cards, whether to attack or not, and where to attack.
    
  CONTINENTAL : Uses RandomStrategy, AggressiveStrategy, CardsStrategy, ContinentalStrategy
    Expanded GREEDY
    Uses ContinentsStrategy to claim initial territories.

  FURIOUS : Uses RandomStrategy, AggressiveStrategy, CardsStrategy, ContinentalStrategy, SmartAggressiveStrategy
    Expanded CONTINENTAL
    Uses SmartAggressiveStrategy to decide where to place armies, whether to attack or not, and where to attack.

  FOCUSED : Uses RandomStrategy, AggressiveStrategy, CardsStrategy, ContinentalStrategy, SmartAggressiveStrategy, CaptureContinentsStrategy
    Expanded FURIOUS
    Uses Focused strategy to reinforce, place armies and attack on 

List of Strategies:

  PassiveStrategy:
      Only trades in cards when it is forced to.
      Never attacks, never fortifies.
      Always defends with only 1 dice.

  RandomStrategy:
      Randomly claims territories and places armies.
      Randomly decides whether to attack or fortify.
      Attacks and fortifies in random places, with random amounts of armies.

  AggressiveStrategy:
      Always attacks, always uses the maximum number of armies.
      Fortifies to the front lines.
      Always occupies / fortifies with the maximum number of armies.

  CardsStrategy:
      Trades in cards as soon as possible.
      Attacks as little as possible while trying to draw one card per turn. (by capturing a territory)

  ContinentalStrategy:
      Attempts to claim an entire continent intitially by placing armies in the smallest continent with space.

  SmartAggressiveStrategy:
      Places armies onto the front lines.
      Always attacks if it has an advantage. It has an advantage if it has more armies than a bordering enemy territory.

  CaptureContinentsStrategy:
      Focuses on one continent at a time, placing armies in that continent until it captures it.

// Ideas for future agents / strategies

// Think about attacking territories you have surrounded
// Think about forcing towards specific continents
// Think about how fortifying is done, will it always consistently move to the front lines?

// SmartContinental - Avoid clashes by picking continents based on how likely it is to capture the whole thing.
// Cluster - Cluster expands from his biggest set of armies
// Yakool - Yakool builds on Cluster by, focusing on enemies that are too strong, slower attacks than cluster, tries to get a card every turn.
// Boscoe - Boscoe is Yakool with a slowed down attack strategy
// Bort - Bort is slow and steady, 1 attack per turn, slower than boscoe
// Communist - Spreads armies evenly among all owned territories, attacks the weakest enemy territory
// Defendo - Tries to hold on to the territories it starts with, starts by trying to hold a small continent
// Pixie - Attacks continents that have the fewest border points, fortifies to the front lines
// Trotsky - Communist with some extra features
// Vulture - Singles out weak opponents and focuses on eliminating them
// EvilPixie - Pixie, does not fortify to places where it already has an advantage
// KillBot - Combines EvilPixie and Vulture behaviour 
// Shaft - Looks ahead and attacks into as few borders as possible
// Quo - Shaft that tries to obtain a card every turn


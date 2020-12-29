# Treasury

Spigot plugin for limiting and monitoring circulation of currency in a [Vault](https://github.com/MilkBowl/VaultAPI) economy.
Allows players to exchange gold ingots for currency.

I may make it possible to exchange other items for currency in the future, but I chose gold because:
- It's rare enough. It is relatively difficult to find, but not so difficult that most players don't have it.
- It's stable(ish). Gold ore can't be multiplied with fortune enchantments like diamond, making its value more stable.
  - Yes, there are other ways to get gold. If this is an issue for you, you may want to find ways to disable them or track which gold came from ore. This hasn't been a problem on my server yet, but I may end up adding configurable restrictions to this end.
- It isn't used often. In my experience, the number of ways over time that gold is useful in survival play are few enough that it accumulates faster than it can be used. People seem to be willing to part with gold in exchange for something more versatile. Diamonds are a lot more useful, which makes it harder to justify exchanging them especially when trying to organically bootstrap an economy.

Tested with EssentialsX and Vault on a 1.16.4 [Paper](https://papermc.io/) build.

Check out my brother's plugin, [EverGlow](https://github.com/MIdnightfury/EverGlow).

## Usage

Introduce currency into circulation:
```
/circ[ulation] add <amount>
```

View total amount of currency in circulation and amount available for exchange with gold:
```
/circ[ulation]
```

Exchange gold ingots in inventory for currency (this must be executed by an in-game player):
```
/exchange <amount>
```
where `amount` is the number of ingots to exchange.

Note that actual number of ingots exchanged may be less than `amount` if:
- `amount` is higher than the number of ingots in your inventory.
- There is insufficient currency available to exchange.
- There is a bug in my code--please [file an issue](https://github.com/vinhowe/Treasury/issues/new).

## Installation

Download the [latest release](https://github.com/vinhowe/Treasury/releases/latest).

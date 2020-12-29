import net.milkbowl.vault.economy.Economy
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.Integer.min
import java.lang.Integer.parseInt
import kotlin.math.floor
import kotlin.math.roundToInt


class ExchangeCommand(private val treasury: Treasury, val economy: Economy) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§4Only in-game players can use §cexchange§4")
            return true
        }

        if (!sender.hasPermission("treasury.exchange")) {
            sender.sendMessage("§4You do not have access to that command.")
            return true
        }

        if (args.isEmpty() || args.size > 1) {
            return false
        }

        val amount: Int;
        val maxIngots = floor(treasury.reserveBalance / treasury.goldPrice).roundToInt()

        if (maxIngots == 0) {
            sender.sendMessage("§4No currency available for exchange.")
            return true
        }

        try {
            amount = min(parseInt(args[0]), maxIngots)
            if (amount <= 0) {
                sender.sendMessage("§4Please enter a whole number greater than 0.")
                return true
            }
        } catch (e: NumberFormatException) {
            sender.sendMessage("§4${args[0]} is not a whole number.")
            return false
        }

        // Don't allow the player to withdraw more than the available balance
        var ingotsExchangedCount = 0
        for (item in sender.inventory.contents) {
            if (ingotsExchangedCount == amount) {
                break
            }
            if (item != null && item.type == Material.GOLD_INGOT) {
                if (item.amount > ingotsExchangedCount) {
                    val itemsRemovedFromStack = min(amount, item.amount)
                    ingotsExchangedCount += itemsRemovedFromStack
                    item.amount -= ingotsExchangedCount
                }
            }
        }

        if (ingotsExchangedCount == 0) {
            sender.sendMessage("§4No gold found in your inventory.")
            return true
        }

        val exchangedAmount = ingotsExchangedCount * treasury.goldPrice;
        economy.depositPlayer(sender, exchangedAmount)
        treasury.reserveBalance -= exchangedAmount;
        sender.sendMessage(
            "§6Exchanged $ingotsExchangedCount gold ingot${if (ingotsExchangedCount != 1) "s" else ""} for $${
                "%.2f".format(
                    exchangedAmount
                )
            }."
        )

        return true
    }
}
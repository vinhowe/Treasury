import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import java.lang.Double.parseDouble


class CirculationCommand(private val treasury: Treasury) : TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>? {
        if (args.size == 1) {
            return listOf("add")
        }

        return null
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (!sender.hasPermission("treasury.circulation")) {
                sender.sendMessage("§4You do not have access to that command.")
                return true
            }
        }

        if (args.isEmpty()) {
            sender.sendMessage("§6Total currency: $${"%.2f".format(treasury.totalCurrency)}");
            sender.sendMessage("§6Reserve balance: $${"%.2f".format(treasury.reserveBalance)}");
            return true
        }

        // sender.sendMessage(args[0])
        if (args[0] == "add") {
            if (args.size != 2) {
                return false;
            }

            val amount: Double;
            try {
                amount = parseDouble(args[1])
            } catch (e: NumberFormatException) {
                sender.sendMessage("§4${args[1]} is not a number.")
                return false
            }

            treasury.totalCurrency += amount;
            treasury.reserveBalance += amount;
            sender.sendMessage("§6$${"%.2f".format(amount)} added to circulation.")
            return true
        }

        return false
    }
}
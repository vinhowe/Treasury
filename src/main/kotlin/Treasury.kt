import net.milkbowl.vault.economy.Economy
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.nio.file.Paths


class Treasury : JavaPlugin() {
    private lateinit var economy: Economy
    val circulationPath: File = Paths.get(dataFolder.path, "circulation.yml").toFile()
    val circulationConfig = YamlConfiguration.loadConfiguration(circulationPath)

    override fun onEnable() {
        if (!setupEconomy()) {
            logger.severe(
                String.format(
                    "[%s] - Disabled due to no Vault dependency found!",
                    description.name
                )
            )
            server.pluginManager.disablePlugin(this)
            return
        }
        getCommand("circulation")?.setExecutor(CirculationCommand(this))
        getCommand("exchange")?.setExecutor(ExchangeCommand(this, economy))
    }

    override fun onDisable() {
        logger.info(
            String.format(
                "[%s] Disabled Version %s",
                description.name,
                description.version
            )
        )
    }

    private fun setupEconomy(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp: RegisteredServiceProvider<Economy> = server.servicesManager.getRegistration(Economy::class.java)
            ?: return false
        economy = rsp.provider
        @Suppress("SENSELESS_COMPARISON")
        return economy != null
    }

    val goldPrice: Double
        get() {
            return config.get("gold-price") as Double? ?: 0.0
        }

    private fun getCirculationValue(key: String, defaultValue: Any?): Any? {
        val circulationValue = circulationConfig.get(key)
        if (circulationValue == null) {
            setCirculationValue(key, defaultValue)
            return defaultValue
        }
        return circulationValue
    }

    private fun setCirculationValue(key: String, value: Any?) {
        circulationConfig.set(key, value);
        circulationConfig.save(circulationPath)
    }

    var totalCurrency: Double
        get() {
            return (getCirculationValue("total-currency", 0.0) as Double?) ?: 0.0
        }
        set(value) {
            setCirculationValue("total-currency", value)
        }

    var reserveBalance: Double
        get() {
            return (getCirculationValue("reserve-balance", 0.0) as Double?) ?: 0.0
        }
        set(value) {
            setCirculationValue("reserve-balance", value)
        }
}
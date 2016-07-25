package net.lomeli.mcslack.core.handler

import com.google.gson.Gson
import net.lomeli.mcslack.MCSlack
import net.minecraft.entity.player.EntityPlayer
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import java.net.URLEncoder

object SlackPostHelper {
    private var gson: Gson = Gson()

    fun sentSlackMessage(player: EntityPlayer, message: String) {
        val httpClient = HttpClientBuilder.create().build()
        try {
            val request = HttpPost(MCSlack.modConfig.incomingHook)
            val msg = SlackMessage(player.displayNameString, message, "https://mcapi.ca/avatar/2d/${player.displayNameString}/100/false")
            val json = URLEncoder.encode(gson.toJson(msg), "UTF-8")
            val param = StringEntity("payload=${json}")
            request.addHeader("content-type", "application/x-www-form-urlencoded")
            request.entity = param
            val response = httpClient.execute(request)
        } catch (ex: Exception) {
            MCSlack.logger.logError("Something happened while sending message!");
            ex.printStackTrace()
        } finally {
            httpClient.close()
        }
    }
}
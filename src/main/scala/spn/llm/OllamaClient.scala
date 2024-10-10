package spn.llm

import requests.Response
import spn.dsl.Reflection.reflect

object OllamaClient extends App:

  private val ollamaUrl = "http://localhost:11434/api/generate"

  private val model = "spn-llama3-model"
  private val prompt = "Provide me a simple Stochastic Petri Net with 5 places and 2 transitions"
  private val data: String = s"""{ "model": "$model", "prompt": "$prompt", "stream": false }"""
  val response: Response = requests.post(
    ollamaUrl,
    data = data,
    readTimeout = 60000,
    connectTimeout = 60000
  )

  if response.statusCode == 200 then
    val test = ujson.read(response.data.toString)
    println(test("response").str)
    println(reflect(test("response").str))
  else println(s"Error: ${response.statusCode} ${response.statusMessage}")

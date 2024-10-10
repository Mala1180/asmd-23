package spn.llm

import requests.Response
import spn.dsl.Reflection.reflect


object OllamaClient extends App:

  private enum Model(val name: String):
    case Llama3 extends Model("spn-llama3-model")
    case Gemma2 extends Model("spn-gemma2-model")
  
  private val ollamaUrl = "http://localhost:11434/api/generate"

  private val model = Model.Gemma2.name
  private val prompt = "Provide me a Stochastic Petri Net representing a Stochastic Mutual Exclusion system with various rates"
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

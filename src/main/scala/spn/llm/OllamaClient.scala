package spn.llm

import requests.Response

object OllamaClient extends App:

  enum Model(val name: String):
    case Llama3 extends Model("spn-llama3-model")
    case Gemma2 extends Model("spn-gemma2-model")

  private val prompt =
    "Provide me a Stochastic Petri Net representing a Stochastic Mutual Exclusion system with various rates"

  def askToModel(model: Model, prompt: String): String =
    val ollamaUrl = "http://localhost:11434/api/generate"
    val data: String = s"""{ "model": "${model.name}", "prompt": "$prompt", "stream": false }"""
    val response: Response = requests.post(
      ollamaUrl,
      data = data,
      readTimeout = 60000,
      connectTimeout = 60000
    )
    if response.statusCode == 200 then
      val test = ujson.read(response.data.toString)
      test("response").str
    else throw new Exception(s"Error: ${response.statusCode} ${response.statusMessage}")

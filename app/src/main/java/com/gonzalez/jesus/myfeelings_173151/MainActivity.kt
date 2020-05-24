package com.gonzalez.jesus.myfeelings_173151


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.gonzalez.jesus.myfeelings_173151.utilities.CustomBarDrawable
import com.gonzalez.jesus.myfeelings_173151.utilities.CustomCircleDrawable
import com.gonzalez.jesus.myfeelings_173151.utilities.JSONFile
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var jsonFile: JSONFile? = null
    var veryHappy = 0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var verysad = 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun parseJson (jsonArray: JSONArray): ArrayList<Emociones> {

            var lista = ArrayList<Emociones>()

            for (i in 0..jsonArray.length()) {
                try {
                    val nombre = jsonArray.getJSONObject(i).getString("nombre")
                    val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                    var color = jsonArray.getJSONObject(i).getInt("color")
                    val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                    var emocion = Emociones(nombre, porcentaje, color, total)
                    lista.add(emocion)
                } catch (exception: JSONException) {
                    exception.printStackTrace()
                }
            }
            return lista
        }

        fun actualizarGrafica() {

            val total = veryHappy+happy+neutral+sad+verysad

            var pVH: Float = (veryHappy * 100 / total).toFloat()
            var pH: Float = (happy * 100 / total).toFloat()
            var pN: Float = (neutral * 100 / total).toFloat()
            var pS: Float = (sad * 100 / total).toFloat()
            var pVS: Float = (verysad * 100 / total).toFloat()

            Log.d("porcentajes", "very happy"+ pVH)
            Log.d("porcentajes", "happy"+ pH)
            Log.d("porcentajes", "neutral"+ pN)
            Log.d("porcentajes", "sad"+ pS)
            Log.d("porcentajes", "very sad"+ pVS)

            lista.clear()
            lista.add(Emociones("Muy feliz", pVH, R.color.mustard,veryHappy))
            lista.add(Emociones("Feliz", pH, R.color.mustard,happy))
            lista.add(Emociones("Neutral", pN, R.color.mustard,neutral))
            lista.add(Emociones("Triste", pS, R.color.mustard,sad))
            lista.add(Emociones("Muy triste", pVS, R.color.mustard,verysad))

            val fondo = CustomCircleDrawable(this, lista)

            graphVeryHappy.background = CustomBarDrawable(this, Emociones ("Muy feliz", pVH, R.color.mustard, veryHappy))
            graphHappy.background = CustomBarDrawable(this, Emociones ("Feliz", pVH, R.color.mustard, happy))
            graphNeutral.background = CustomBarDrawable(this, Emociones ("Neutral", pVH, R.color.mustard, neutral))
            graphSad.background = CustomBarDrawable(this, Emociones ("Triste", pVH, R.color.mustard, sad))
            graphVerySad.background = CustomBarDrawable(this, Emociones ("Muy triste", pVH, R.color.mustard, verysad))

            graph.background = fondo

        }

        fun fetchingData() {

            try {
                var json: String = jsonFile?.getData(this) ?: ""
                if (json != "") {
                    this.data = true
                    var jsonArray:JSONArray = JSONArray(json)

                    this.lista = parseJson(jsonArray)

                    for (i in lista)
                        when (i.nombre) {
                            "Muy feliz" -> veryHappy = i.total
                            "Feliz" -> happy = i.total
                            "Neutral" -> neutral = i.total
                            "Triste" -> sad = i.total
                            "Muy triste" -> verysad = i.total
                        }
                } else {
                    this.data = false
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }

        }

        fun iconoMayoria() {
            if(happy>veryHappy && happy>neutral && happy>sad && sad>verysad){
                icon.setImageDrawable(resources.getDrawable(R.drawable.ic_happy))
            }
            if(veryHappy>happy && veryHappy>neutral && veryHappy>sad && veryHappy>verysad){
                icon.setImageDrawable(resources.getDrawable(R.drawable.ic_veryhappy))
            }
            if(neutral>veryHappy && neutral>happy && neutral>sad && neutral>verysad){
                icon.setImageDrawable(resources.getDrawable(R.drawable.ic_neutral))
            }
            if(sad>happy && sad>neutral && sad>veryHappy && sad>verysad){
                icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sad))
            }
            if(verysad>happy && verysad>neutral && verysad>sad && veryHappy<verysad){
                icon.setImageDrawable(resources.getDrawable(R.drawable.ic_very_dissatisfied))
            }

        }

        fun guardar(){
            var jsonArray = JSONArray()
            var o: Int = 0
            for (i in lista){
                Log.d("objetos", i.toString())
                var j: JSONObject = JSONObject()
                j.put("nombre", i.nombre)
                j.put("porcentaje", i.porcentaje)
                j.put("color", i.color)
                j.put("total", i.total)

                jsonArray.put(o, j)
                o++
            }

            jsonFile?.saveData(this, jsonArray.toString())

            Toast.makeText(this, "Datos Guardados", Toast.LENGTH_SHORT).show()
        }
    }




}

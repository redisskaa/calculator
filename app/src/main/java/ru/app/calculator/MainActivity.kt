package ru.app.calculator

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private var expression = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        // Привязка цифровых кнопок и точки
        setupNumberButtons()

        // Операторы
        findViewById<MaterialButton>(R.id.btnAdd).setOnClickListener { append("+") }
        findViewById<MaterialButton>(R.id.btnSubtract).setOnClickListener { append("−") }
        findViewById<MaterialButton>(R.id.btnMultiply).setOnClickListener { append("×") }
        findViewById<MaterialButton>(R.id.btnDivide).setOnClickListener { append("÷") }
        findViewById<MaterialButton>(R.id.btnPercent).setOnClickListener { append("%") }
        findViewById<MaterialButton>(R.id.btnLeftParen).setOnClickListener { append("(") }
        findViewById<MaterialButton>(R.id.btnRightParen).setOnClickListener { append(")") }
        findViewById<MaterialButton>(R.id.btnDot).setOnClickListener { append(".") }

        // Кнопка очистки
        findViewById<MaterialButton>(R.id.btnClear).setOnClickListener {
            expression.clear()
            updateExpression()
            tvResult.text = "0"
        }

        // Кнопка равно
        findViewById<MaterialButton>(R.id.btnEqual).setOnClickListener { calculate() }

        findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun setupNumberButtons() {
        val numberButtons = listOf(
            R.id.btn0 to "0",
            R.id.btn1 to "1",
            R.id.btn2 to "2",
            R.id.btn3 to "3",
            R.id.btn4 to "4",
            R.id.btn5 to "5",
            R.id.btn6 to "6",
            R.id.btn7 to "7",
            R.id.btn8 to "8",
            R.id.btn9 to "9"
        )
        numberButtons.forEach { (id, text) ->
            findViewById<MaterialButton>(id).setOnClickListener { append(text) }
        }
    }

    private fun append(text: String) {
        expression.append(text)
        updateExpression()
    }

    private fun calculate() {
        try {
            val expr = expression.toString()
                .replace("×", "*")
                .replace("÷", "/")
                .replace("%", "/100")

            val result = ExpressionBuilder(expr).build().evaluate()
            tvResult.text = if (result.toLong() == result.toLong()) result.toLong().toString() else result.toString()
            expression.clear() // Опционально: очистить после вычисления
            expression.append(tvResult.text)
        } catch (e: Exception) {
            tvResult.text = "Ошибка"
        }
    }

    private fun updateExpression() {
        tvExpression.text = expression.toString()
    }
}
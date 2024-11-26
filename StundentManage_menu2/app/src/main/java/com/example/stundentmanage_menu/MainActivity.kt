package com.example.stundentmanage_menu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import android.content.Intent
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var lv: ListView
    private val REQUEST_CODE_ADD = 100
    private val REQUEST_CODE_EDIT = 101

    val students = mutableListOf(
        Student("Nguyễn Văn An", "SV001"),
        Student("Trần Thị Bảo", "SV002"),
        Student("Lê Hoàng Cường", "SV003"),
        Student("Phạm Thị Dung", "SV004"),
        Student("Đỗ Minh Đức", "SV005"),
        Student("Vũ Thị Hoa", "SV006"),
        Student("Hoàng Văn Hải", "SV007"),
        Student("Bùi Thị Hạnh", "SV008"),
        Student("Đinh Văn Hùng", "SV009"),
        Student("Nguyễn Thị Linh", "SV010"),
        Student("Phạm Văn Long", "SV011"),
        Student("Trần Thị Mai", "SV012"),
        Student("Lê Thị Ngọc", "SV013"),
        Student("Vũ Văn Nam", "SV014"),
        Student("Hoàng Thị Phương", "SV015"),
        Student("Đỗ Văn Quân", "SV016"),
        Student("Nguyễn Thị Thu", "SV017"),
        Student("Trần Văn Tài", "SV018"),
        Student("Phạm Thị Tuyết", "SV019"),
        Student("Lê Văn Vũ", "SV020")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        lv = findViewById(R.id.lv)
        val adapter = StudentAdapter(this, R.layout.layout_item, students)
        lv.adapter = adapter
        registerForContextMenu(lv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                val intent = Intent(this, AddStudentActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_ADD)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            val name = data?.getStringExtra("name")
            val mssv = data?.getStringExtra("mssv")

            if (name != null && mssv != null) {
                students.add(Student(name, mssv))
                (lv.adapter as StudentAdapter).notifyDataSetChanged()
            }
        }

        // Xử lý dữ liệu trả về từ EditStudentActivity
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            val updatedName = data?.getStringExtra("name")
            val updatedMssv = data?.getStringExtra("mssv")
            val position = data?.getIntExtra("position", -1)

            if (updatedName != null && updatedMssv != null && position != null && position >= 0) {
                // Cập nhật đối tượng trong myList tại vị trí đã chỉnh sửa
                val student = students[position]
                student.name = updatedName
                student.mssv = updatedMssv

                // Thông báo cập nhật cho Adapter
                (lv.adapter as StudentAdapter).notifyDataSetChanged()
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position

        return when (item.itemId) {
            R.id.menu_edit -> {
                val student = students[position]
                val intent = Intent(this, EditStudentActivity::class.java)
                intent.putExtra("name", student.name)
                intent.putExtra("mssv", student.mssv)
                intent.putExtra("position", position)
                startActivityForResult(intent, REQUEST_CODE_EDIT)
                true
            }
            R.id.menu_remove -> {
                students.removeAt(position)
                (lv.adapter as StudentAdapter).notifyDataSetChanged()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

}
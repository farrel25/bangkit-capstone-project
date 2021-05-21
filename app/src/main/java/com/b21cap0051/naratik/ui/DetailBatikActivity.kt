package com.b21cap0051.naratik.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.b21cap0051.naratik.adapter.ArticleMiniListAdapter
import com.b21cap0051.naratik.adapter.BatikMiniListAdapter
import com.b21cap0051.naratik.databinding.ActivityDetailBatikBinding
import com.b21cap0051.naratik.dataresource.datamodellist.BatikModel
import com.b21cap0051.naratik.util.DataDummy
import com.b21cap0051.naratik.util.ItemBatikCallBack

class DetailBatikActivity : AppCompatActivity(),ItemBatikCallBack {
    private lateinit var binding : ActivityDetailBatikBinding
    private lateinit var batikAdapter : BatikMiniListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBatikBinding.inflate(layoutInflater)
        setContentView(binding.root)
    
        batikAdapter = BatikMiniListAdapter(this)
        binding.rvVmBatik.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.rvVmBatik.adapter = batikAdapter
        val listBatik = DataDummy.generateDummyBatik()
        batikAdapter.setList(listBatik)
    }
    
    override fun itemBatikClick(model : BatikModel)
    {
        TODO("Not yet implemented")
    }
}
package com.b21cap0051.naratik.ui.home.homefragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.b21cap0051.naratik.R
import com.b21cap0051.naratik.adapter.ArticleListAdapter
import com.b21cap0051.naratik.adapter.BatikPagedListAdapter
import com.b21cap0051.naratik.adapter.ShimmerBatikListAdapter
import com.b21cap0051.naratik.databinding.FragmentExploreBinding
import com.b21cap0051.naratik.databinding.ItemRowBatikBinding
import com.b21cap0051.naratik.dataresource.datamodellist.ArticleModel
import com.b21cap0051.naratik.dataresource.datamodellist.ShimmerModel
import com.b21cap0051.naratik.dataresource.local.model.BatikEntity
import com.b21cap0051.naratik.mainview.BatikMainView
import com.b21cap0051.naratik.mainview.ViewFactoryModel
import com.b21cap0051.naratik.ui.ArticleActivity
import com.b21cap0051.naratik.ui.BatikActivity
import com.b21cap0051.naratik.ui.SearchActivity
import com.b21cap0051.naratik.util.DataDummy
import com.b21cap0051.naratik.util.ItemArticleCallBack
import com.b21cap0051.naratik.util.ItemBatikCallBack
import com.b21cap0051.naratik.util.naratikDependencys
import com.b21cap0051.naratik.util.vo.Status


class ExploreFragment : Fragment() , ItemBatikCallBack , ItemArticleCallBack
{
	
	private var _binding : FragmentExploreBinding? = null
	private val binding get() = _binding as FragmentExploreBinding
	private var listArticle : ArrayList<ArticleModel> = arrayListOf()
	
	private var listShimmer : ArrayList<ShimmerModel> = arrayListOf()
	private lateinit var adapterArticle : ArticleListAdapter
	private lateinit var adapterBatik : BatikPagedListAdapter
	private lateinit var adapterShimmer : ShimmerBatikListAdapter
	
	
	companion object
	{
		val TAG : String = ExploreFragment::class.java.simpleName
		const val EXTRA_ARTICLE = "extra_article"
		const val EXTRA_BATIK = "extra_batik"
	}
	
	override fun onCreateView(
		inflater : LayoutInflater ,
		container : ViewGroup? ,
		savedInstanceState : Bundle?
	                         ) : View
	{
		_binding = FragmentExploreBinding.inflate(layoutInflater , container , false)
		return binding.root
	}
	
	private lateinit var mainView : BatikMainView
	
	override fun onViewCreated(view : View , savedInstanceState : Bundle?)
	{
		super.onViewCreated(view , savedInstanceState)
		
		val factory = ViewFactoryModel(naratikDependencys.injectRepository(requireActivity()))
		mainView = ViewModelProvider(requireActivity() , factory)[BatikMainView::class.java]
		loadShimmerBatikList()
		
		mainView.getLimitedBatik().observe(viewLifecycleOwner , { response ->
			when (response.Status)
			{
				Status.SUCCESS ->
				{
					loadListBatik(response.Data!!)
				}
				Status.LOADING ->
				{
				
				}
				Status.ERROR   ->
				{
					Toast.makeText(requireContext() , "Get Data To API Error!" , Toast.LENGTH_SHORT)
						.show()
				}
			}
			
		})
		
		binding.svSearch.isEnabled = true
		binding.svSearch.setOnClickListener {
			val move = Intent(requireContext() , SearchActivity::class.java)
			startActivity(move)
			onPause()
		
		}
		
		
		loadListArticle()
	}
	
	private fun loadListBatik(value : PagedList<BatikEntity>)
	{
		adapterBatik = BatikPagedListAdapter(this)
		
		
		var row = 2
		if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			row = 4
		}
		
		binding.rvBatik.layoutManager =
			StaggeredGridLayoutManager(row , StaggeredGridLayoutManager.VERTICAL)
		binding.rvBatik.adapter = adapterBatik
		adapterBatik.submitList(value)


		binding.shimmerLayout.visibility = View.GONE
		
		
		
		binding.btnShowAllBatik.setOnClickListener {
			val intent = Intent(requireActivity() , BatikActivity::class.java)
			startActivity(intent)
		}
	}
	
	private fun loadShimmerBatikList()
	{
		var row = 2
		if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			row = 4
		}
		adapterShimmer = ShimmerBatikListAdapter()

		listShimmer = DataDummy.generateShimmerBatik()
		binding.rvShimmer.layoutManager = StaggeredGridLayoutManager(row , StaggeredGridLayoutManager.VERTICAL)
		binding.rvShimmer.adapter = adapterShimmer

		adapterShimmer.setList(listShimmer)
	}
	
	private fun loadListArticle()
	{
		adapterArticle = ArticleListAdapter(this)
		
		binding.rvArticle.layoutManager =
			LinearLayoutManager(activity , LinearLayoutManager.HORIZONTAL , false)
		binding.rvArticle.adapter = adapterArticle
		listArticle = DataDummy.generateDummyArticle()
		adapterArticle.setList(listArticle)
		
		binding.btnShowAllArticle.setOnClickListener {
			val intent = Intent(requireActivity() , ArticleActivity::class.java)
			startActivity(intent)
		}
	}
	
	
	
	override fun itemBatikClick(model : BatikEntity)
	{
	
	}
	
	override fun AddFavour(v : ItemRowBatikBinding , model : BatikEntity)
	{
		if(CheckIsFavor(model)){
			val modelbaru = BatikEntity(
				model.batik_id,
				model.name_batik,
				model.makna_batik,
				model.Image,
				model.daerah_batik,
				0
			                           )
			mainView.addFavor(modelbaru)
			v.btnItemFavBatik.setBackgroundColor(R.drawable.ic_love_outlined)
			adapterBatik.notifyDataSetChanged()
		}else{
			val modelbaru = BatikEntity(
				model.batik_id,
				model.name_batik,
				model.makna_batik,
				model.Image,
				model.daerah_batik,
				1
			                           )
			mainView.addFavor(modelbaru)
			v.btnItemFavBatik.setBackgroundColor(R.drawable.ic_love_filled)
			adapterBatik.notifyDataSetChanged()
		}
	}
	
	override fun CheckIsFavor(model : BatikEntity) : Boolean
	{
		var stat = false
		mainView.getFavourAllbatik().observe(viewLifecycleOwner,{
				response ->
			when(response.Status){
				Status.SUCCESS -> {
					for (i in  0 until response.Data?.size!!){
						if(response.Data[i]?.batik_id == model.batik_id){
							Log.d("TESAPP","$response.Data[i]?.batik_id")
							stat = true
							Log.d("TESAPP","$stat")
							break
						}
					}
				}
			}
		})
		return stat
	}
	
	override fun itemArticleClick(model : ArticleModel)
	{
	
	}
}
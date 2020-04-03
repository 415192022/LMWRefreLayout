package com.lmw.widget.lmwrefrelayout

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lmw.widget.lmwrefrelayout.adapter.UserAdapter
import com.lmw.widget.lmwrefreshlayout.lib.listener.OnPullToRefreshListener
import com.lmw.widget.lmwrefrelayout.model.User
import kotlinx.android.synthetic.main.fragment_scroll.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ScrollFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScrollFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ScrollFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var mData: ArrayList<User> = arrayListOf(
            User("jenny"),
            User("sum"),
            User("sun"),
            User("money"),
            User("jason"),
            User("jackson"),
            User("blues"),
            User("link"),
            User("ken"),
            User("js"),
            User("break"),
            User("private"),
            User("public"),
            User("protect"),
            User("class"),
            User("bush")
    )

    private var adapter: UserAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scroll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = UserAdapter(context, mData)
        recycler.adapter = adapter

        refreshLayout.setPullToRefreshListener(object : OnPullToRefreshListener {
            override fun onRefresh() {
                refreshLayout.postDelayed({
                    Log.e("onRefresh", "下拉刷新")
                    mData.clear()
                    mData.addAll(getNewData())
                    adapter?.notifyDataSetChanged()
                    refreshLayout.onFinish()
                }, 3000)
            }

            override fun onLoadMore() {
                refreshLayout.postDelayed({
                    Log.e("onRefresh", "上拉加载")
                    refreshLayout.onFinish()
                    mData.addAll(getNewData())
                    adapter?.notifyDataSetChanged()
                }, 3000)
            }

        })

    }

    fun getNewData(): ArrayList<User> {
        return arrayListOf(
                User("jenny"),
                User("sum"),
                User("sun"),
                User("money"),
                User("jason"),
                User("jackson"),
                User("blues"),
                User("link"),
                User("ken"),
                User("js"),
                User("break"),
                User("private"),
                User("public"),
                User("protect"),
                User("class"),
                User("bush")
        )
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScrollFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScrollFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

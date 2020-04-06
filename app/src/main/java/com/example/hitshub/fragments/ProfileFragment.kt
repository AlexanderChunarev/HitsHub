package com.example.hitshub.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.example.hitshub.R
import com.example.hitshub.activities.BaseActivity
import com.example.hitshub.activities.RegisterActivity
import com.example.hitshub.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private val user by lazy { activity!!.intent.getSerializableExtra(BaseActivity.USER) as User }
    private val motionLayout by lazy { activity!!.findViewById<MotionLayout>(R.id.motion_base) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.findViewById<FrameLayout>(R.id.mini_player_container).setOnClickListener {
            motionLayout.transitionToEnd()
        }

        button_exit.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity!!, RegisterActivity::class.java))
            activity!!.finishAffinity()
        }
    }
}

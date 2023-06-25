package com.example.myfamilysafty

import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*


class HomeFragment : Fragment() {

   private  val listContacts :ArrayList<ContactModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val listMembers = listOf<MemberModel>(
            MemberModel(
                "Aman",
                "D-27, Mangla puri village , Palam ,near singhal hospital ,Delhi",
                "90%",
                "220"
            ),
            MemberModel(
                "Namita",
                "Vinod Homes , gali no 14 , Near pakoda park , palam ,delhi",
                "80%",
                "210"
            ),
            MemberModel(
                "Tushar",
                "D-27, Mangla puri village , Palam ,near singhal hospital ,Delhi",
                "70%",
                "200"
            ),
            MemberModel(
                "Ankush",
                "Near sai mandir, ground floor , jaurasi raod , Samalka Panipat, Harayana",
                "60%",
                "190"
            ),
            MemberModel(
                "Tamana",
                "Near sai mandir, ground floor , jaurasi raod , Samalka Panipat",
                "80%",
                "70"
            ),
            MemberModel(
                "Gaurav",
                "pooth kalan, near bhram shakti hospital ,ground floor ,delhi",
                "80%",
                "20"
            ),
            MemberModel(
                "Sonia",
                "Bhiwani , rothak , Harayana",
                "100%",
                "250"
            ),
            MemberModel(
                "Annu",
                "pooth kalan, near bhram shakti hospital ,ground floor ,delhi",
                "60%",
                "190"
            ),
        )

        val adapter = MemberAdapter(listMembers)

        val recycler = requireView().findViewById<RecyclerView>(R.id.recycler_member)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        val inviteAdapter = InviteAdapter(listContacts)



       CoroutineScope(Dispatchers.IO).launch {
           listContacts.addAll(fetchContacts())
           fetchDatabaseContacts(listContacts)
           withContext(Dispatchers.Main){
               inviteAdapter.notifyDataSetChanged()
           }

       }
        val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        inviteRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inviteRecycler.adapter = inviteAdapter
    }

    private suspend fun fetchDatabaseContacts(listContacts: ArrayList<ContactModel>) {
        val databse = MyFamilyDataBase.getDatabase(requireContext())

        databse.contactDao().insertAll(listContacts)
    }

    private fun fetchContacts(): ArrayList<ContactModel> {
        val cr = requireActivity().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        val listContacts: ArrayList<ContactModel> = ArrayList()

        if (cursor != null && cursor.count > 0) {

            while (cursor != null && cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhoneNumber > 0) {

                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        ""
                    )

                    if (pCur != null && pCur.count > 0) {

                        while (pCur != null && pCur.moveToNext()) {

                            val phoneNum =
                                pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            listContacts.add(ContactModel(name, phoneNum))

                        }
                        pCur.close()

                    }

                }
            }

            if (cursor != null) {
                cursor.close()
            }
        }
            return listContacts
    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}


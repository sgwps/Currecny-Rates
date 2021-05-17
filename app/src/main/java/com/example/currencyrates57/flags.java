package com.example.currencyrates57;

public class flags {
    static int flags[] = {R.drawable.ic_aud, R.drawable.ic_bgn, R.drawable.ic_brl, R.drawable.ic_cad, R.drawable.ic_chf, R.drawable.ic_cny,
    R.drawable.ic_czk, R.drawable.ic_dkk, R.drawable.ic_eur, R.drawable.ic_gbp, R.drawable.ic_hkd, R.drawable.ic_hrk,
    R.drawable.ic_huf, R.drawable.ic_idr, R.drawable.ic_ils, R.drawable.ic_inr, R.drawable.ic_isk, R.drawable.ic_jpy,
    R.drawable.ic_krw, R.drawable.ic_mxn, R.drawable.ic_myr, R.drawable.ic_nok, R.drawable.ic_nzd, R.drawable.ic_php, R.drawable.ic_pln,
    R.drawable.ic_ron, R.drawable.ic_rub, R.drawable.ic_sek, R.drawable.ic_sgd, R.drawable.ic_thb,
    R.drawable.ic_try, R.drawable.ic_usd, R.drawable.ic_zar
    };

    public static int get_flag(int pos){
        return flags[pos];
    }
}

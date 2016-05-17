package com.example.evan.evansmyruns.classifier;

/**
 * Classifier for the activity types in Automatic mode.
 *
 * Obsolete, but kept for reference - the code is now using the WekaClassifierNew class.
 *
 */
public class WekaClassifier {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier.Ne94b480(i);
        return p;
    }
    static double Ne94b480(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 97.808383) {
            p = WekaClassifier.N10308d51(i);
        } else if (((Double) i[0]).doubleValue() > 97.808383) {
            p = WekaClassifier.Nda713b8(i);
        }
        return p;
    }
    static double N10308d51(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 56.468866) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 56.468866) {
            p = WekaClassifier.N19bb3ff2(i);
        }
        return p;
    }
    static double N19bb3ff2(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 0;
        } else if (((Double) i[5]).doubleValue() <= 4.023527) {
            p = WekaClassifier.N2b25273(i);
        } else if (((Double) i[5]).doubleValue() > 4.023527) {
            p = WekaClassifier.N1a63bde6(i);
        }
        return p;
    }
    static double N2b25273(Object []i) {
        double p = Double.NaN;
        if (i[16] == null) {
            p = 0;
        } else if (((Double) i[16]).doubleValue() <= 0.929057) {
            p = WekaClassifier.Nf087614(i);
        } else if (((Double) i[16]).doubleValue() > 0.929057) {
            p = 1;
        }
        return p;
    }
    static double Nf087614(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 2.245486) {
            p = WekaClassifier.N106750a5(i);
        } else if (((Double) i[5]).doubleValue() > 2.245486) {
            p = 0;
        }
        return p;
    }
    static double N106750a5(Object []i) {
        double p = Double.NaN;
        if (i[13] == null) {
            p = 1;
        } else if (((Double) i[13]).doubleValue() <= 0.511849) {
            p = 1;
        } else if (((Double) i[13]).doubleValue() > 0.511849) {
            p = 0;
        }
        return p;
    }
    static double N1a63bde6(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 91.762368) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 91.762368) {
            p = WekaClassifier.N2d31107(i);
        }
        return p;
    }
    static double N2d31107(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 93.431571) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 93.431571) {
            p = 0;
        }
        return p;
    }
    static double Nda713b8(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 23.411446) {
            p = WekaClassifier.N4cf94f9(i);
        } else if (((Double) i[64]).doubleValue() > 23.411446) {
            p = WekaClassifier.Nb9baf452(i);
        }
        return p;
    }
    static double N4cf94f9(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 408.786919) {
            p = WekaClassifier.Naa4e1310(i);
        } else if (((Double) i[0]).doubleValue() > 408.786919) {
            p = WekaClassifier.N32c42530(i);
        }
        return p;
    }
    static double Naa4e1310(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 154.41042) {
            p = WekaClassifier.N1404d2711(i);
        } else if (((Double) i[0]).doubleValue() > 154.41042) {
            p = WekaClassifier.N1f35b7221(i);
        }
        return p;
    }
    static double N1404d2711(Object []i) {
        double p = Double.NaN;
        if (i[8] == null) {
            p = 1;
        } else if (((Double) i[8]).doubleValue() <= 5.145342) {
            p = WekaClassifier.Ndaa9dd12(i);
        } else if (((Double) i[8]).doubleValue() > 5.145342) {
            p = WekaClassifier.N6b527a20(i);
        }
        return p;
    }
    static double Ndaa9dd12(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() <= 0.65656) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() > 0.65656) {
            p = WekaClassifier.N119a94813(i);
        }
        return p;
    }
    static double N119a94813(Object []i) {
        double p = Double.NaN;
        if (i[20] == null) {
            p = 0;
        } else if (((Double) i[20]).doubleValue() <= 0.75447) {
            p = WekaClassifier.N12daa6e14(i);
        } else if (((Double) i[20]).doubleValue() > 0.75447) {
            p = WekaClassifier.N16f96cb17(i);
        }
        return p;
    }
    static double N12daa6e14(Object []i) {
        double p = Double.NaN;
        if (i[26] == null) {
            p = 1;
        } else if (((Double) i[26]).doubleValue() <= 0.326057) {
            p = 1;
        } else if (((Double) i[26]).doubleValue() > 0.326057) {
            p = WekaClassifier.Nf5e41915(i);
        }
        return p;
    }
    static double Nf5e41915(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 4.657075) {
            p = WekaClassifier.N18e3d9b16(i);
        } else if (((Double) i[5]).doubleValue() > 4.657075) {
            p = 0;
        }
        return p;
    }
    static double N18e3d9b16(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 0;
        } else if (((Double) i[11]).doubleValue() <= 1.068691) {
            p = 0;
        } else if (((Double) i[11]).doubleValue() > 1.068691) {
            p = 1;
        }
        return p;
    }
    static double N16f96cb17(Object []i) {
        double p = Double.NaN;
        if (i[27] == null) {
            p = 0;
        } else if (((Double) i[27]).doubleValue() <= 0.609954) {
            p = 0;
        } else if (((Double) i[27]).doubleValue() > 0.609954) {
            p = WekaClassifier.N1ed35d218(i);
        }
        return p;
    }
    static double N1ed35d218(Object []i) {
        double p = Double.NaN;
        if (i[21] == null) {
            p = 1;
        } else if (((Double) i[21]).doubleValue() <= 1.642047) {
            p = 1;
        } else if (((Double) i[21]).doubleValue() > 1.642047) {
            p = WekaClassifier.Nf7d4da19(i);
        }
        return p;
    }
    static double Nf7d4da19(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 146.140511) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 146.140511) {
            p = 0;
        }
        return p;
    }
    static double N6b527a20(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 3;
        } else if (((Double) i[5]).doubleValue() <= 8.746299) {
            p = 3;
        } else if (((Double) i[5]).doubleValue() > 8.746299) {
            p = 0;
        }
        return p;
    }
    static double N1f35b7221(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() <= 9.014993) {
            p = WekaClassifier.N118ab3b22(i);
        } else if (((Double) i[7]).doubleValue() > 9.014993) {
            p = WekaClassifier.N1ab920124(i);
        }
        return p;
    }
    static double N118ab3b22(Object []i) {
        double p = Double.NaN;
        if (i[17] == null) {
            p = 1;
        } else if (((Double) i[17]).doubleValue() <= 3.133429) {
            p = 1;
        } else if (((Double) i[17]).doubleValue() > 3.133429) {
            p = WekaClassifier.N1f9fdc623(i);
        }
        return p;
    }
    static double N1f9fdc623(Object []i) {
        double p = Double.NaN;
        if (i[30] == null) {
            p = 3;
        } else if (((Double) i[30]).doubleValue() <= 2.076167) {
            p = 3;
        } else if (((Double) i[30]).doubleValue() > 2.076167) {
            p = 1;
        }
        return p;
    }
    static double N1ab920124(Object []i) {
        double p = Double.NaN;
        if (i[19] == null) {
            p = 0;
        } else if (((Double) i[19]).doubleValue() <= 2.574) {
            p = WekaClassifier.Na871cd25(i);
        } else if (((Double) i[19]).doubleValue() > 2.574) {
            p = WekaClassifier.N35469026(i);
        }
        return p;
    }
    static double Na871cd25(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 253.506483) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 253.506483) {
            p = 2;
        }
        return p;
    }
    static double N35469026(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (((Double) i[4]).doubleValue() <= 15.752861) {
            p = WekaClassifier.N44821d27(i);
        } else if (((Double) i[4]).doubleValue() > 15.752861) {
            p = WekaClassifier.N12031ce28(i);
        }
        return p;
    }
    static double N44821d27(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 268.616547) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 268.616547) {
            p = 3;
        }
        return p;
    }
    static double N12031ce28(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 224.776002) {
            p = WekaClassifier.N6d17be29(i);
        } else if (((Double) i[0]).doubleValue() > 224.776002) {
            p = 1;
        }
        return p;
    }
    static double N6d17be29(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 16.242801) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() > 16.242801) {
            p = 0;
        }
        return p;
    }
    static double N32c42530(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (((Double) i[4]).doubleValue() <= 55.037411) {
            p = WekaClassifier.Nd0e55431(i);
        } else if (((Double) i[4]).doubleValue() > 55.037411) {
            p = 2;
        }
        return p;
    }
    static double Nd0e55431(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() <= 77.190396) {
            p = WekaClassifier.Nb2030132(i);
        } else if (((Double) i[3]).doubleValue() > 77.190396) {
            p = 2;
        }
        return p;
    }
    static double Nb2030132(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 138.172024) {
            p = WekaClassifier.Nac240e33(i);
        } else if (((Double) i[1]).doubleValue() > 138.172024) {
            p = WekaClassifier.N2ff73746(i);
        }
        return p;
    }
    static double Nac240e33(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 843.19071) {
            p = WekaClassifier.Nd1d25234(i);
        } else if (((Double) i[0]).doubleValue() > 843.19071) {
            p = WekaClassifier.N1aa24444(i);
        }
        return p;
    }
    static double Nd1d25234(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() <= 23.107694) {
            p = WekaClassifier.N1e2691935(i);
        } else if (((Double) i[3]).doubleValue() > 23.107694) {
            p = WekaClassifier.Nfb121e37(i);
        }
        return p;
    }
    static double N1e2691935(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 3;
        } else if (((Double) i[9]).doubleValue() <= 2.221622) {
            p = WekaClassifier.N15ad56f36(i);
        } else if (((Double) i[9]).doubleValue() > 2.221622) {
            p = 1;
        }
        return p;
    }
    static double N15ad56f36(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 7.129213) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() > 7.129213) {
            p = 3;
        }
        return p;
    }
    static double Nfb121e37(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 2;
        } else if (((Double) i[64]).doubleValue() <= 17.687681) {
            p = WekaClassifier.N14099138(i);
        } else if (((Double) i[64]).doubleValue() > 17.687681) {
            p = WekaClassifier.N1e0659043(i);
        }
        return p;
    }
    static double N14099138(Object []i) {
        double p = Double.NaN;
        if (i[30] == null) {
            p = 2;
        } else if (((Double) i[30]).doubleValue() <= 3.658568) {
            p = WekaClassifier.N460d2b39(i);
        } else if (((Double) i[30]).doubleValue() > 3.658568) {
            p = WekaClassifier.N17f108142(i);
        }
        return p;
    }
    static double N460d2b39(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 10.6331) {
            p = WekaClassifier.N19841e140(i);
        } else if (((Double) i[5]).doubleValue() > 10.6331) {
            p = WekaClassifier.N1ffddee41(i);
        }
        return p;
    }
    static double N19841e140(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 34.644174) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() > 34.644174) {
            p = 1;
        }
        return p;
    }
    static double N1ffddee41(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 2;
        } else if (((Double) i[12]).doubleValue() <= 6.406253) {
            p = 2;
        } else if (((Double) i[12]).doubleValue() > 6.406253) {
            p = 3;
        }
        return p;
    }
    static double N17f108142(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() <= 19.307313) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() > 19.307313) {
            p = 2;
        }
        return p;
    }
    static double N1e0659043(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 2;
        } else if (((Double) i[12]).doubleValue() <= 14.902342) {
            p = 2;
        } else if (((Double) i[12]).doubleValue() > 14.902342) {
            p = 3;
        }
        return p;
    }
    static double N1aa24444(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 59.277572) {
            p = WekaClassifier.N1570bc445(i);
        } else if (((Double) i[1]).doubleValue() > 59.277572) {
            p = 1;
        }
        return p;
    }
    static double N1570bc445(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (((Double) i[4]).doubleValue() <= 19.336748) {
            p = 1;
        } else if (((Double) i[4]).doubleValue() > 19.336748) {
            p = 2;
        }
        return p;
    }
    static double N2ff73746(Object []i) {
        double p = Double.NaN;
        if (i[13] == null) {
            p = 2;
        } else if (((Double) i[13]).doubleValue() <= 2.35101) {
            p = WekaClassifier.N2bd14747(i);
        } else if (((Double) i[13]).doubleValue() > 2.35101) {
            p = WekaClassifier.N1504d8448(i);
        }
        return p;
    }
    static double N2bd14747(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 819.650835) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 819.650835) {
            p = 1;
        }
        return p;
    }
    static double N1504d8448(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 3;
        } else if (((Double) i[9]).doubleValue() <= 6.981039) {
            p = WekaClassifier.N1a770149(i);
        } else if (((Double) i[9]).doubleValue() > 6.981039) {
            p = WekaClassifier.N18bbe7a50(i);
        }
        return p;
    }
    static double N1a770149(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() <= 6.108815) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() > 6.108815) {
            p = 3;
        }
        return p;
    }
    static double N18bbe7a50(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() <= 11.26509) {
            p = WekaClassifier.N1c4e62551(i);
        } else if (((Double) i[12]).doubleValue() > 11.26509) {
            p = 1;
        }
        return p;
    }
    static double N1c4e62551(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() <= 15.14629) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() > 15.14629) {
            p = 3;
        }
        return p;
    }
    static double Nb9baf452(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() <= 388.802198) {
            p = WekaClassifier.N148629d53(i);
        } else if (((Double) i[1]).doubleValue() > 388.802198) {
            p = WekaClassifier.N4d950c62(i);
        }
        return p;
    }
    static double N148629d53(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 3;
        } else if (((Double) i[5]).doubleValue() <= 15.162216) {
            p = WekaClassifier.N1ea7be054(i);
        } else if (((Double) i[5]).doubleValue() > 15.162216) {
            p = WekaClassifier.N151543655(i);
        }
        return p;
    }
    static double N1ea7be054(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 919.801434) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 919.801434) {
            p = 3;
        }
        return p;
    }
    static double N151543655(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 1043.363609) {
            p = WekaClassifier.Nda3d3256(i);
        } else if (((Double) i[0]).doubleValue() > 1043.363609) {
            p = WekaClassifier.N1ecae6f59(i);
        }
        return p;
    }
    static double Nda3d3256(Object []i) {
        double p = Double.NaN;
        if (i[19] == null) {
            p = 2;
        } else if (((Double) i[19]).doubleValue() <= 10.786504) {
            p = 2;
        } else if (((Double) i[19]).doubleValue() > 10.786504) {
            p = WekaClassifier.Nbeea7e57(i);
        }
        return p;
    }
    static double Nbeea7e57(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() <= 193.760465) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() > 193.760465) {
            p = WekaClassifier.N79a2b258(i);
        }
        return p;
    }
    static double N79a2b258(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 148.77177) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() > 148.77177) {
            p = 2;
        }
        return p;
    }
    static double N1ecae6f59(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 3;
        } else if (((Double) i[64]).doubleValue() <= 33.282928) {
            p = 3;
        } else if (((Double) i[64]).doubleValue() > 33.282928) {
            p = WekaClassifier.N4244d60(i);
        }
        return p;
    }
    static double N4244d60(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 2;
        } else if (((Double) i[10]).doubleValue() <= 13.285833) {
            p = 2;
        } else if (((Double) i[10]).doubleValue() > 13.285833) {
            p = WekaClassifier.N12cdce961(i);
        }
        return p;
    }
    static double N12cdce961(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 3;
        } else if (((Double) i[4]).doubleValue() <= 64.166722) {
            p = 3;
        } else if (((Double) i[4]).doubleValue() > 64.166722) {
            p = 2;
        }
        return p;
    }
    static double N4d950c62(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 2;
        } else if (((Double) i[6]).doubleValue() <= 17.062058) {
            p = 2;
        } else if (((Double) i[6]).doubleValue() > 17.062058) {
            p = 3;
        }
        return p;
    }
}


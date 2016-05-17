package com.example.evan.evansmyruns.classifier;

/* A data-classification class created using Waikato Knowledge Environment
*
* For activity classification in Automatic mode.
* */

public class WekaClassifierNew {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifierNew.N179ab040(i);
        return p;
    }
    static double N179ab040(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 356.102035) {
            p = WekaClassifierNew.N114897c1(i);
        } else if (((Double) i[0]).doubleValue() > 356.102035) {
            p = WekaClassifierNew.N1077a2a8(i);
        }
        return p;
    }
    static double N114897c1(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 95.762372) {
            p = WekaClassifierNew.N15d117a2(i);
        } else if (((Double) i[0]).doubleValue() > 95.762372) {
            p = WekaClassifierNew.N2485ed4(i);
        }
        return p;
    }
    static double N15d117a2(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 80.273673) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 80.273673) {
            p = WekaClassifierNew.N6b222f3(i);
        }
        return p;
    }
    static double N6b222f3(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 0;
        } else if (((Double) i[1]).doubleValue() <= 39.565785) {
            p = 0;
        } else if (((Double) i[1]).doubleValue() > 39.565785) {
            p = 1;
        }
        return p;
    }
    static double N2485ed4(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 118.459901) {
            p = WekaClassifierNew.N188b37c5(i);
        } else if (((Double) i[0]).doubleValue() > 118.459901) {
            p = 1;
        }
        return p;
    }
    static double N188b37c5(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() <= 10.592307) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() > 10.592307) {
            p = WekaClassifierNew.N65ba366(i);
        }
        return p;
    }
    static double N65ba366(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 0;
        } else if (((Double) i[1]).doubleValue() <= 27.207868) {
            p = WekaClassifierNew.N12c83cf7(i);
        } else if (((Double) i[1]).doubleValue() > 27.207868) {
            p = 1;
        }
        return p;
    }
    static double N12c83cf7(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() <= 1.228784) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() > 1.228784) {
            p = 0;
        }
        return p;
    }
    static double N1077a2a8(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 1688.352795) {
            p = WekaClassifierNew.N1a621e59(i);
        } else if (((Double) i[0]).doubleValue() > 1688.352795) {
            p = WekaClassifierNew.N6041ea20(i);
        }
        return p;
    }
    static double N1a621e59(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 2;
        } else if (((Double) i[64]).doubleValue() <= 14.378585) {
            p = WekaClassifierNew.Nf3926d10(i);
        } else if (((Double) i[64]).doubleValue() > 14.378585) {
            p = WekaClassifierNew.N1ed85c914(i);
        }
        return p;
    }
    static double Nf3926d10(Object []i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() <= 1.564579) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() > 1.564579) {
            p = WekaClassifierNew.N15a4ac711(i);
        }
        return p;
    }
    static double N15a4ac711(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 86.145514) {
            p = WekaClassifierNew.N168b42c12(i);
        } else if (((Double) i[2]).doubleValue() > 86.145514) {
            p = 1;
        }
        return p;
    }
    static double N168b42c12(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 387.840888) {
            p = WekaClassifierNew.N19d0aad13(i);
        } else if (((Double) i[0]).doubleValue() > 387.840888) {
            p = 2;
        }
        return p;
    }
    static double N19d0aad13(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() <= 24.180874) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() > 24.180874) {
            p = 3;
        }
        return p;
    }
    static double N1ed85c914(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 2;
        } else if (((Double) i[6]).doubleValue() <= 104.627781) {
            p = WekaClassifierNew.N24ec8115(i);
        } else if (((Double) i[6]).doubleValue() > 104.627781) {
            p = WekaClassifierNew.N1d9fd7a19(i);
        }
        return p;
    }
    static double N24ec8115(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() <= 286.974564) {
            p = WekaClassifierNew.N5bfe3d16(i);
        } else if (((Double) i[1]).doubleValue() > 286.974564) {
            p = 2;
        }
        return p;
    }
    static double N5bfe3d16(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 1281.815122) {
            p = WekaClassifierNew.N1d44d7917(i);
        } else if (((Double) i[0]).doubleValue() > 1281.815122) {
            p = 3;
        }
        return p;
    }
    static double N1d44d7917(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 530.987803) {
            p = WekaClassifierNew.Nc4520718(i);
        } else if (((Double) i[0]).doubleValue() > 530.987803) {
            p = 2;
        }
        return p;
    }
    static double Nc4520718(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() <= 153.159375) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() > 153.159375) {
            p = 3;
        }
        return p;
    }
    static double N1d9fd7a19(Object []i) {
        double p = Double.NaN;
        if (i[20] == null) {
            p = 3;
        } else if (((Double) i[20]).doubleValue() <= 27.606305) {
            p = 3;
        } else if (((Double) i[20]).doubleValue() > 27.606305) {
            p = 2;
        }
        return p;
    }
    static double N6041ea20(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 3;
        } else if (((Double) i[1]).doubleValue() <= 444.804146) {
            p = 3;
        } else if (((Double) i[1]).doubleValue() > 444.804146) {
            p = WekaClassifierNew.N1f679da21(i);
        }
        return p;
    }
    static double N1f679da21(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 1843.572035) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 1843.572035) {
            p = 3;
        }
        return p;
    }
}
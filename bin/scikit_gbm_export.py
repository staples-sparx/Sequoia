import numpy as np


def gbm_export_model(model, x, attributes, filename):
    with open(filename, 'w') as fd:
        fd.write("GBM MODEL\n")
        fd.write("initF = %0.15g\n" % model.init_.predict(x[0])[0])
        fd.write("learning_rate = %0.15g\n" % model.learning_rate)
        fd.write("vars = ")
        fd.writelines(','.join(attributes))
        fd.write("\n")
        fd.write("nsplitlists = 0\n")
        fd.write("ntrees = %d\n" % model.n_estimators)
        for i in range(model.n_estimators):
            tree = model.estimators_[i][0].tree_
            fd.write("tree %d\n" % tree.node_count)
            var = tree.feature
            var[var < 0] = -1
            thr = tree.threshold
            thr[var < 0] = np.nan
            lft = tree.children_left
            rgt = tree.children_right
            mss = np.zeros(tree.node_count)
            prd = tree.value.flatten()
            prd[np.log(np.abs(prd)) > np.log(1e+10)] = np.nan
            prd[np.log(np.abs(prd)) < np.log(1e-10)] = np.nan
            for j in range(tree.node_count):
                fd.write("%d,%0.15g,%d,%d,%d,%0.15g\n" % (var[j], thr[j], lft[j], rgt[j], mss[j], prd[j]))
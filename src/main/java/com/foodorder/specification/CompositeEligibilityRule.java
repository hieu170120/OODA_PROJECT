package com.foodorder.specification;

import com.foodorder.model.Order;

import java.util.ArrayList;
import java.util.List;

public class CompositeEligibilityRule implements EligibilityRule {
    private List<EligibilityRule> rules;

    public CompositeEligibilityRule() {
        this.rules = new ArrayList<>();
    }

    public CompositeEligibilityRule(List<EligibilityRule> rules) {
        this.rules = (rules == null) ? new ArrayList<>() : new ArrayList<>(rules);
    }

    public void addRule(EligibilityRule rule) {
        if (rule != null) {
            this.rules.add(rule);
        }
    }

    @Override
    public boolean isSatisfiedBy(Order order) {
        if (rules == null || rules.isEmpty()) {
            return true;
        }

        for (EligibilityRule rule : rules) {
            if (!rule.isSatisfiedBy(order)) {
                return false;
            }
        }
        return true;
    }

    public List<EligibilityRule> getRules() {
        return rules;
    }

    public void setRules(List<EligibilityRule> rules) {
        this.rules = (rules == null) ? new ArrayList<>() : new ArrayList<>(rules);
    }
}

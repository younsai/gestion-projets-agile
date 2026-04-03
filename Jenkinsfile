pipeline {
    agent any // utiliser n'importe quel agent disponible

    environment {
        APP_NAME = 'mon-application'
        DEPLOY_DIR = '/opt/apps/mon-application'
    }

    options {
        timeout(time: 30, unit: 'MINUTES') // annuler si trop long
        disableConcurrentBuilds() // un seul build à la fois
        buildDiscarder(logRotator(numToKeepStr: '10')) // garder 10 builds
    }

    stages {

        stage('Checkout') {
            steps {
// Jenkins récupère automatiquement le code du dépôt configuré
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        stage('Tests unitaires') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
// Publier les résultats JUnit dans l'interface Jenkins
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {

            steps {
                sh 'mvn package -DskipTests'
// Archiver le JAR produit comme artifact du build
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Déploiement staging') {
            when {
                branch 'main' // seulement sur la branche main
            }
            steps {
                sh """
mkdir -p ${DEPLOY_DIR}
cp target/${APP_NAME}-*.jar ${DEPLOY_DIR}/app.jar
systemctl restart ${APP_NAME}
"""
            }
        }

        stage('Déploiement production') {

            when {
                branch 'main'
            }
            input {
                message "Déployer en production ?"
                ok "Oui, déployer"
                submitter "admin,tech-lead"
            }
            steps {
                sh "./scripts/deploy-prod.sh ${BUILD_NUMBER}"
            }
        }
    }

    post {

        success {
            echo "Build #${BUILD_NUMBER} réussi"
// emailext to: 'team@example.com', subject: 'Build OK', body: '...'
        }
        failure {
            echo "Build #${BUILD_NUMBER} échoué"
        }
        always {
            cleanWs() // nettoyer le workspace après chaque build
        }
    }
}